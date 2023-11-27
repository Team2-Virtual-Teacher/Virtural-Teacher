package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.RatingDto;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.CourseDto;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.CourseDtoMapper;
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
    private final CourseDtoMapper courseMapper;

    @Autowired
    public CourseController(CourseService courseService, AuthenticationHelper authenticationHelper, CourseDtoMapper courseMapper) {
        this.courseService = courseService;
        this.authenticationHelper = authenticationHelper;
        this.courseMapper = courseMapper;
    }


    @GetMapping
    public List<Course> get(
            @RequestHeader(required = false) HttpHeaders headers,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String teacher,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder
    ) {
        FilterOptions filterOptions = new FilterOptions( title,  topic, teacher,  rating,  sortBy, sortOrder);
        return courseService.get(filterOptions);
    }


    @GetMapping("/{id}")
    public Course get(@PathVariable (name = "id") int id){
       return courseService.getCourseById(id);
    }


    @GetMapping("/enrolled")
    public List<Course> getUsersEnrolledCourses(@RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return courseService.getUsersEnrolledCourses(user.getUserId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PostMapping("/{id}/enroll")
    public void enrollUserForCourse(@RequestHeader HttpHeaders headers, @PathVariable (name = "id") int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            courseService.enrollUserForCourse(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
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

    @PostMapping("/{id}/rate")
    public void rateCourse(@RequestHeader HttpHeaders headers, @PathVariable int id,  @RequestBody RatingDto rating) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            courseService.rateCourse(rating, id, user.getUserId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/transfer/from/{userIdFrom}/to/{userIdTo}")
    public void transferCourses(@RequestHeader HttpHeaders headers, @PathVariable int userIdFrom,
                                @PathVariable int userIdTo){
        try {
            User user = authenticationHelper.tryGetUser(headers);
             courseService.transferTeacherCourses(userIdFrom, userIdTo, user);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        } catch (UnsupportedOperationException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
