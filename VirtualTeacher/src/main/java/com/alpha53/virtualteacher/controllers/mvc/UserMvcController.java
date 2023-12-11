package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.EmailForm;
import com.alpha53.virtualteacher.services.contracts.UserService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
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
            model.addAttribute("error", e.getMessage());
            // TODO: 11.12.23 add this page.
            return "401";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "ReferralView";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "401";
        }
    }

}
