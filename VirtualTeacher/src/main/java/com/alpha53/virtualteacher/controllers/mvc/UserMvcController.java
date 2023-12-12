package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.exceptions.StorageException;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.EmailForm;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import com.alpha53.virtualteacher.services.contracts.UserService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.UserMapperHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final CourseService courseService;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, CourseService courseService, UserMapperHelper userMapperHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.courseService = courseService;
    }

    @GetMapping("/{id}/profile")
    public String showUserPage(HttpSession session, Model model, @PathVariable int id) {
        // TODO: 11.12.23 consider rewriting this later on.
        User loggedUser;
        User userToGet;
        try {
            loggedUser = authenticationHelper.tryGetCurrentUser(session);
            userToGet = userService.get(id);
            if (loggedUser.getUserId() != userToGet.getUserId()) {
                model.addAttribute("errorMessage", "Unauthorized access.");
                model.addAttribute("statusCode", 404);
                return "4xx";
            }
            model.addAttribute("userProfile", userToGet);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }
        return "UserPageView";
    }

    @GetMapping("/{id}/courses/enrolled")
    public String showUserEnrolledCourses(HttpSession session, Model model, @PathVariable int id) {
        // TODO: 11.12.23 consider rewriting this later on.
        User loggedUser;
        User userToGet;
        try {
            loggedUser = authenticationHelper.tryGetCurrentUser(session);
            userToGet = userService.get(id);
            if (loggedUser.getUserId() != userToGet.getUserId()) {
                model.addAttribute("errorMessage", "Unauthorized access.");
                model.addAttribute("statusCode", 404);
                return "4xx";
            }
            model.addAttribute("userProfile", userToGet);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }
        return "UserPageEnrolledCoursesView";
    }

    @GetMapping("/{id}/courses/completed")
    public String showUserCompletedCourses(HttpSession session, Model model, @PathVariable int id) {
        // TODO: 11.12.23 consider rewriting this later on.
        User loggedUser;
        User userToGet;
        try {
            loggedUser = authenticationHelper.tryGetCurrentUser(session);
            userToGet = userService.get(id);
            if (loggedUser.getUserId() != userToGet.getUserId()) {
                model.addAttribute("errorMessage", "Unauthorized access.");
                model.addAttribute("statusCode", 404);
                return "4xx";
            }
            model.addAttribute("userProfile", userToGet);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }

        model.addAttribute("completedCourses", courseService.getUsersCompletedCourses(id));
        return "UserPageCompletedCoursesView";
    }

    @GetMapping("/{id}/settings")
    public String showUserUpdatePage(HttpSession session, Model model, @PathVariable int id) {
        User loggedUser;
        User userToGet;
        try {
            loggedUser = authenticationHelper.tryGetCurrentUser(session);
            userToGet = userService.get(id);
            if (loggedUser.getUserId() != userToGet.getUserId()) {
                model.addAttribute("errorMessage", "Unauthorized access.");
                model.addAttribute("statusCode", 404);
                return "4xx";
            }
            model.addAttribute("userProfile", userToGet);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }
        UserDto userDto = new UserDto();
        userDto.setFirstName(userToGet.getFirstName());
        userDto.setLastName(userToGet.getLastName());
        userDto.setEmail(userToGet.getEmail());
        userDto.setPassword(userToGet.getPassword());
        userDto.setRole(userToGet.getRole().getRoleType());
        model.addAttribute("userDto", userDto);
        return "UserSettingsView";
    }

    @PostMapping("/{id}/settings")
    public String handleUpdateUser(@Valid @ModelAttribute("userDto") UserDto updateDto, BindingResult bindingResult, HttpSession session, Model model,
                                    @PathVariable int id) {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("userProfile", userService.get(id));
            if (bindingResult.hasErrors()) {
                return "UserSettingsView";
            }
            userService.update(updateDto,loggedInUser,id);
            return "redirect:/users/{id}/profile";
        } catch (AuthorizationException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 401);
            return "4xx";
        }
        // TODO: 12.12.23 catch entity not found

    }

    @PostMapping("{id}/settings/picture")
    public String uploadProfilePicture(HttpSession session, @PathVariable int id, Model model, @RequestParam("file") MultipartFile file) {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            userService.uploadProfilePicture(file, loggedInUser, id);
            model.addAttribute("userProfile", userService.get(id));
            session.setAttribute("currentUser", userService.get(id));
            return "redirect:/users/{id}/profile";

        } catch (AuthorizationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 401);
            return "4xx";
        } catch (StorageException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 422);
            return "4xx";
        }
        // TODO: 12.12.23 catch entitynotfoundexception
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
