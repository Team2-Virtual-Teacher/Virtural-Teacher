package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.LoginDto;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.services.contracts.UserService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.UserMapperHelper;
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
    private final UserMapperHelper userMapperHelper;

    private final UserService userService;

    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserMapperHelper userMapperHelper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapperHelper = userMapperHelper;
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
            session.setAttribute("currentUserEmail", user.getEmail());
            session.setAttribute("currentUser", user);

            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new UserDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") UserDto registerDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }

        User user = userMapperHelper.userDtoToUser(registerDto);

        try {
            userService.create(user, registerDto.getRole());
            return "EmailConfirmationView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "RegisterView";
        } catch (EntityNotFoundException e) {
            bindingResult.rejectValue("role", "role_error", e.getMessage());
            return "RegisterView";
            // TODO: 10.12.23 this also throws an illegalStateException because of the sending of the email.
        }
    }
}
