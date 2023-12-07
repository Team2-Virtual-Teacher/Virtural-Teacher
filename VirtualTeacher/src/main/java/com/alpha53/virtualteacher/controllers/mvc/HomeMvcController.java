package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import com.alpha53.virtualteacher.services.contracts.TopicService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final CourseService courseService;
    private final TopicService topicService;

    public HomeMvcController(CourseService courseService, TopicService topicService) {
        this.courseService = courseService;
        this.topicService = topicService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("filterOptions", new FilterOptions());
        model.addAttribute("coursesCount", courseService.getCoursesCount());
        model.addAttribute("topics",topicService.getAll());


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
