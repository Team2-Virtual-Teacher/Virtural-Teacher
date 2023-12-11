package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.EmailForm;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import com.alpha53.virtualteacher.services.contracts.UserService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final CourseService courseService;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, CourseService courseService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.courseService = courseService;
    }

    @GetMapping("/{id}/profile")
    public String showUserPage(HttpSession session, Model model, @PathVariable int id) {
        // TODO: 11.12.23 consider rewriting this later on.
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            User userToGet = userService.get(id);
            if (loggedUser.getUserId() != userToGet.getUserId()){
                model.addAttribute("errorMessage", "Unauthorized access.");
                model.addAttribute("statusCode", 404);
                return "4xx";
            }
            model.addAttribute("userProfile", userToGet);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }
        return "UserPageView";
    }

    @GetMapping("/{id}/courses/enrolled")
    public String showUserEnrolledCourses(HttpSession session, Model model, @PathVariable int id) {
        // TODO: 11.12.23 consider rewriting this later on.
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            User userToGet = userService.get(id);
            if (loggedUser.getUserId() != userToGet.getUserId()){
                model.addAttribute("errorMessage", "Unauthorized access.");
                model.addAttribute("statusCode", 404);
                return "4xx";
            }
            model.addAttribute("userProfile", userToGet);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }
        return "UserPageEnrolledCoursesView";
    }

    @GetMapping("/{id}/courses/completed")
    public String showUserCompletedCourses(HttpSession session, Model model, @PathVariable int id) {
        // TODO: 11.12.23 consider rewriting this later on.
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            User userToGet = userService.get(id);
            if (loggedUser.getUserId() != userToGet.getUserId()){
                model.addAttribute("errorMessage", "Unauthorized access.");
                model.addAttribute("statusCode", 404);
                return "4xx";
            }
            model.addAttribute("userProfile", userToGet);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }

        model.addAttribute("completedCourses", courseService.getUsersCompletedCourses(id));
        return "UserPageCompletedCoursesView";
    }


    @GetMapping("/referral")
    public String showReferralPage(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "ReferralView";
    }

    @PostMapping("/referral")
    public String handleReferral(@Valid @ModelAttribute("emailForm") EmailForm emailForm,
                                 BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "ReferralView";
        }

        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            userService.referFriend(loggedInUser, emailForm.getEmail());
            return "ReferralConfirmationView";
        } catch (AuthorizationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 401);
            // TODO: 11.12.23 add this page.
            return "4xx";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "ReferralView";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 422);
            return "4xx";
        }
    }

}
