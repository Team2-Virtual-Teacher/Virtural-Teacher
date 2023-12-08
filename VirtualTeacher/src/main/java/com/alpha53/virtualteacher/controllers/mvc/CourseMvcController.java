package com.alpha53.virtualteacher.controllers.mvc;


import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseMvcController {
    private final CourseService courseService;
    private final AuthenticationHelper authenticationHelper;

    public CourseMvcController(CourseService courseService, AuthenticationHelper authenticationHelper) {
        this.courseService = courseService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showAllCourses( @RequestParam(required = false) String title,
                                  @RequestParam(required = false) String topic,
                                  @RequestParam(required = false) String teacher,
                                  @RequestParam(required = false) String rating,
                                  @RequestParam(required = false) String isPublic,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder,
                                  Model model,
                                  HttpSession session
    ) {
        Boolean isPublicBool;
        if (isPublic == null) {
            isPublicBool = null;
        } else {
            isPublicBool = Boolean.parseBoolean(isPublic);
        }
        FilterOptions filterOptions = new FilterOptions(title, topic, teacher, rating, isPublicBool, sortBy, sortOrder);
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = Optional.of(authenticationHelper.tryGetCurrentUser(session));

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException ignored) {

        }
        List<Course> courses =courseService.get(filterOptions, optionalUser);


        model.addAttribute("courses", courses);

        return "CoursesView";

    }

    @GetMapping("/{id}")
    public String get(
            @RequestHeader(required = false) HttpHeaders headers, @PathVariable(name = "id") int id, Model model, HttpSession session) {
        Course course;
        try {

            User user = authenticationHelper.tryGetCurrentUser(session);
            course = courseService.getCourseByIdAuth(id, user);

            if (courseService.isUserEnrolled(user.getUserId(), course.getCourseId())){
                model.addAttribute("isEnrolled", true);
                model.addAttribute("hasPassed", false);
            } else if (courseService.hasUserPassedCourse(user.getUserId(), course.getCourseId())){
                model.addAttribute("hasPassed", true);
                model.addAttribute("isEnrolled", false);
            } else {
                model.addAttribute("hasPassed", false);
                model.addAttribute("isEnrolled", false);
            }

            if (user.getRole().getRoleType().equalsIgnoreCase("admin") || (user.getRole().getRoleType().equalsIgnoreCase("teacher") && course.getCreator().getUserId() == user.getUserId())){
                model.addAttribute("hasModifyPermissions", true);
            } else {
                model.addAttribute("hasModifyPermissions", false);
            }

            model.addAttribute("course", course);
            model.addAttribute("currentUserRole",  session.getAttribute("currentUserRole"));
            model.addAttribute("isStudent", session.getAttribute("isStudent"));


            return "SingleCourseView";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            course = courseService.getCourseById(id);
            model.addAttribute("course", course);
            return "SingleCourseView";
        }
    }

    @GetMapping("/{id}/enroll")
    public String enrollForCourse(@RequestHeader(required = false) HttpHeaders headers, @PathVariable(name = "id") int id, Model model, HttpSession session) {



        try {
            User user = authenticationHelper.tryGetCurrentUser(session);

            courseService.enrollUserForCourse(user, id);
            return "redirect:/courses/{id}";

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
}
