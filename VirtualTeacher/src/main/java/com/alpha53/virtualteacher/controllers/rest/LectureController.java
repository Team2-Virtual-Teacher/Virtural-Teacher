package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.LectureDto;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.LectureDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class LectureController {
    private final LectureService lectureService;
    private final LectureDtoMapper lectureDtoMapper;
    private final AuthenticationHelper authenticationHelper;

    public LectureController(LectureService lectureService, LectureDtoMapper lectureDtoMapper, AuthenticationHelper authenticationHelper) {
        this.lectureService = lectureService;
        this.lectureDtoMapper = lectureDtoMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{courseId}/lecture/{lectureId}")
    public Lecture get(@RequestHeader HttpHeaders headers,
                       @PathVariable(name = "courseId") int courseId,
                       @PathVariable(name = "lectureId") int lectureId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return lectureService.get(courseId,lectureId, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    /*Tested with Postman*/
    @GetMapping("/{courseId}/lecture")
    public List<Lecture> getAllByCourse(@RequestHeader HttpHeaders headers,
                                        @PathVariable(name = "courseId") int courseId) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            return lectureService.getAllByCourseId(courseId, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    /*Tested with Postman*/
    @PostMapping("{id}/lecture")
    public void create(@RequestHeader HttpHeaders headers,
                       @RequestBody @Valid LectureDto lectureDto,
                       @PathVariable(name = "id") int courseId) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            Lecture lectureToCreate = lectureDtoMapper.dtoToObject(lectureDto);
            lectureToCreate.setCourseId(courseId);
            lectureService.create(lectureToCreate, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /*Tested with Postman*/
    @PutMapping("{courseId}/lecture/{id}")
    public void update(@RequestHeader HttpHeaders headers,
                       @RequestBody @Valid LectureDto lectureDto,
                       @PathVariable(name = "courseId") int courseId,
                       @PathVariable(name = "id") int id
    ) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            Lecture updateLecture = lectureDtoMapper.dtoToObject(lectureDto);
            updateLecture.setCourseId(courseId);
            updateLecture.setId(id);
            lectureService.update(updateLecture, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    /*Tested with Postman*/
    //TODO check why courseId is not used
    @DeleteMapping("{courseId}/lecture/{id}")
    public void delete(@RequestHeader HttpHeaders headers,
                       @PathVariable(name = "id") int id,
                       @PathVariable(name = "courseId") int courseId) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            lectureService.delete(id, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}

