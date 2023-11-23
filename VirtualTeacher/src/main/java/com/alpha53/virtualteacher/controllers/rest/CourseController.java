package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.CourseDto;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final AuthenticationHelper authenticationHelper;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseController(CourseService courseService, AuthenticationHelper authenticationHelper, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.authenticationHelper = authenticationHelper;
        this.courseMapper = courseMapper;
    }


    @GetMapping()
    public List<Course> getAll() {
       return courseService.getAllCourses();
    }


    @GetMapping("/{id}")
    public Course get(@PathVariable (name = "id") int id){
       return courseService.getCourseById(id);
    }

    @PostMapping()
    public void create(@RequestHeader HttpHeaders headers,  @RequestBody CourseDto courseDto){


        try {
            Course course = courseMapper.fromDto(courseDto);
            User user = authenticationHelper.tryGetUser(headers);
            courseService.create(course, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestBody CourseDto courseDto){


        try {
            Course course = courseMapper.fromDto(id, courseDto);
            User user = authenticationHelper.tryGetUser(headers);
            courseService.update(course, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            courseService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
