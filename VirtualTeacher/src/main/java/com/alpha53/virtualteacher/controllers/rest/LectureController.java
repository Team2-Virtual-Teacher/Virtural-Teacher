package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.exceptions.*;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.Solution;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.LectureDto;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import com.alpha53.virtualteacher.services.contracts.SolutionService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.LectureDtoMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@Validated
public class LectureController {
    public static final String INCORRECT_GRADE = "Grade must be in range 2-6";
    private final LectureService lectureService;
    private final LectureDtoMapper lectureDtoMapper;
    private final AuthenticationHelper authenticationHelper;
    private final SolutionService solutionService;


    public LectureController(LectureService lectureService, LectureDtoMapper lectureDtoMapper, AuthenticationHelper authenticationHelper, SolutionService solutionService) {
        this.lectureService = lectureService;
        this.lectureDtoMapper = lectureDtoMapper;
        this.authenticationHelper = authenticationHelper;
        this.solutionService = solutionService;
    }

    @GetMapping(value = "/{courseId}/lecture/{lectureId}")
    public Lecture get(@RequestHeader HttpHeaders headers,
                       @PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                       @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return lectureService.get(courseId, lectureId, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @GetMapping("/{courseId}/lectures")
    public List<Lecture> getAllByCourse(@RequestHeader HttpHeaders headers,
                                        @PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            return lectureService.getAllByCourseId(courseId, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @PostMapping(value = "{id}/lecture", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void create(@RequestHeader HttpHeaders headers,
                       @RequestPart @Valid LectureDto lectureDto,
                       @RequestPart  MultipartFile assignment,
                       @PathVariable(name = "id") @Positive(message = "Course ID must be a positive integer") int courseId) {

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

    @PutMapping("{courseId}/lecture/{id}")
    public void update(@RequestHeader HttpHeaders headers,
                       @RequestPart @Valid LectureDto lectureDto,
                       @RequestPart(required = false) MultipartFile assignment,
                       @PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                       @PathVariable(name = "id") @Positive(message = "Lecture ID must be a positive integer") int id
    ) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            Lecture updateLecture = lectureDtoMapper.dtoToObject(lectureDto);
            updateLecture.setCourseId(courseId);
            updateLecture.setId(id);
            if (lectureDto.getDescription() != null) {
                updateLecture.setDescription(lectureDto.getDescription());
            }
            lectureService.update(updateLecture, user, assignment);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException | UnsupportedFileTypeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @DeleteMapping("{courseId}/lecture/{lectureId}")
    public void delete(@RequestHeader HttpHeaders headers,
                       @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                       @PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId) {

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
                               @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                               @PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                               @RequestPart(name = "solution") MultipartFile solution) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            lectureService.uploadSolution(courseId, lectureId, user, solution);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnsupportedFileTypeException | EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (StorageException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("{courseId}/lecture/{lectureId}/user/{userId}/grade")
    public void addSolutionGrade(@RequestHeader HttpHeaders headers,
                                 @PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                                 @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                 @PathVariable(name = "userId") @Positive(message = "User ID must be a positive integer") int userId,
                                 @RequestParam(name = "grade")
                                 @Min(value = 2, message = INCORRECT_GRADE)
                                 @Max(value = 6, message = INCORRECT_GRADE) double grade) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            Solution solution = new Solution(userId, lectureId, grade);
            solutionService.addSolutionGrade(solution, loggedUser, courseId);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}

