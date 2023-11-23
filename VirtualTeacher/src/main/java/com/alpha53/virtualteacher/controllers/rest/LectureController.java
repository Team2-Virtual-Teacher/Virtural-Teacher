package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LectureController {
private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/course/{id}/lectures")
    public List<Lecture> getAll(@PathVariable (name = "id") int id){
     return lectureService.getAllByCourseId(id);
    }
}

