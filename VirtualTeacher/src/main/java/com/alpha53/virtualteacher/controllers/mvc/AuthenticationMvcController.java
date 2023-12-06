package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.LoginDto;
import com.alpha53.virtualteacher.services.contracts.UserService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "LoginView";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(login.getEmail(), login.getPassword());
            session.setAttribute("currentUser", login.getEmail());
            session.setAttribute("currentUserRole", userService.get(login.getEmail()).getRole().getRoleType());
            session.setAttribute("isAdmin", user.getRole().getRoleType().equalsIgnoreCase("admin"));
            session.setAttribute("isStudent", user.getRole().getRoleType().equalsIgnoreCase("student"));
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "LoginView";
        }
    }
}
