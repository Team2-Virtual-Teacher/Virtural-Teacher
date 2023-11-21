package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping()
    public List<Course> getAll() {
       return courseService.getAll();
    }

    @GetMapping("/{id}")
    public Course get(@PathVariable (name = "id") int id){
       return courseService.get(id);
    }

   /* @PostMapping()
    public void create(@RequestBody CourseDto courseDto){

        courseService.create(courseDto);
    }*/

}
