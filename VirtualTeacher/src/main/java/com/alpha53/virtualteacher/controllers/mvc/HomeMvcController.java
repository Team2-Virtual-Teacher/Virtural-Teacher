package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.FilterOptionDto;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import com.alpha53.virtualteacher.services.contracts.TopicService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.FilterMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final CourseService courseService;
    private final TopicService topicService;
    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(CourseService courseService, TopicService topicService, AuthenticationHelper authenticationHelper) {
        this.courseService = courseService;
        this.topicService = topicService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(@ModelAttribute("filterOptions") FilterOptionDto filterOptionDto, Model model, HttpSession session) {
        model.addAttribute("coursesCount", courseService.getCoursesCount());
        model.addAttribute("topics", topicService.getAll());

        Optional<User> user;

        if (session.getAttribute("currentUser") == null) {
            user = Optional.empty();
        } else {
            user = Optional.ofNullable(authenticationHelper.tryGetCurrentUser(session));
        }
        FilterOptions filterOption = FilterMapper.fromFilterOptionsDtoToFilterOptions(filterOptionDto);

        model.addAttribute("courses", courseService.get(filterOption, user));
        model.addAttribute("filterOptions", filterOptionDto);

        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "aboutView";
    }

    //contacts
    @GetMapping("/contacts")
    public String showContacts() {
        return "contactsView";
    }

}
