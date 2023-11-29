package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.exceptions.*;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.LectureDto;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.LectureDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping(value = "/{courseId}/lecture/{lectureId}" )
    public Lecture get(@RequestHeader HttpHeaders headers,
                       @PathVariable(name = "courseId") int courseId,
                       @PathVariable(name = "lectureId") int lectureId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return lectureService.get(courseId, lectureId, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    /*Tested with Postman*/
    @GetMapping("/{courseId}/lectures")
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
    //TODO Assignment must be uploaded also
    @PostMapping(value = "{id}/lecture",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void create(@RequestHeader HttpHeaders headers,
                       @RequestPart @Valid LectureDto lectureDto,
                       @RequestPart MultipartFile assignment,
                       @PathVariable(name = "id") int courseId) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            Lecture lectureToCreate = lectureDtoMapper.dtoToObject(lectureDto);
            lectureToCreate.setCourseId(courseId);
            lectureService.create(lectureToCreate, user, assignment);

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
    @DeleteMapping("{courseId}/lecture/{lectureId}")
    public void delete(@RequestHeader HttpHeaders headers,
                       @PathVariable(name = "lectureId") int lectureId,
                       @PathVariable(name = "courseId") int courseId) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            lectureService.delete(courseId, lectureId, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping(value = "{courseId}/lecture/{lectureId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadSolution(@RequestHeader HttpHeaders headers,
                                         @PathVariable(name = "lectureId") int lectureId,
                                         @PathVariable(name = "courseId") int courseId,
                                         @RequestPart(name = "Solution") MultipartFile assignmentSolution) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            lectureService.uploadSolution(courseId, lectureId, user, assignmentSolution);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnsupportedFileTypeException | EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        //TODO think for appropriated message to return
        catch (StorageException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}

