package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.services.TopicServiceImpl;
import com.alpha53.virtualteacher.services.contracts.TopicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicServiceImpl topicService) {
        this.topicService = topicService;
    }

    @GetMapping()
    public List<Topic> getAll(){
       return topicService.getAll();
    }
}
