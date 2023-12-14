package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.exceptions.*;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.Solution;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.WikiResult;
import com.alpha53.virtualteacher.models.dtos.UpdateLectureDto;
import com.alpha53.virtualteacher.models.dtos.WikiSearchDto;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import com.alpha53.virtualteacher.services.contracts.*;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.LectureDtoMapper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.UpdateLectureDtoMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/course")
public class LectureMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final LectureService lectureService;
    private final WikiService wikiService;
    private final CourseService courseService;
    private final LectureDtoMapper lectureDtoMapper;
    private final SolutionService solutionService;
    private final SolutionDao solutionDao;

    private final UpdateLectureDtoMapper updateLectureDtoMapper;
    private final UserService userService;

    public LectureMvcController(AuthenticationHelper authenticationHelper, LectureService lectureService,
                                WikiService wikiService, CourseService courseService, LectureDtoMapper lectureDtoMapper, SolutionService solutionService, SolutionDao solutionDao,
                                UpdateLectureDtoMapper updateLectureDtoMapper,
                                UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.lectureService = lectureService;
        this.wikiService = wikiService;
        this.courseService = courseService;
        this.lectureDtoMapper = lectureDtoMapper;
        this.solutionService = solutionService;
        this.solutionDao = solutionDao;
        this.updateLectureDtoMapper = updateLectureDtoMapper;
        this.userService = userService;
    }

    @GetMapping("/{courseId}/lecture/{lectureId}")
    public String singleLecture(@PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                                @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                @ModelAttribute(name = "wikiSearchCriteria") WikiSearchDto wikiSearchDto,
                                Model model,
                                HttpSession session) {

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Lecture lecture = lectureService.get(courseId, lectureId, user);
            model.addAttribute("lecture", lecture);
            model.addAttribute("isAssignmentExist", lectureService.isAssignmentExist(lectureId));
            model.addAttribute("isEnrolled", courseService.isUserEnrolled(user.getUserId(), courseId));
            if (wikiSearchDto.getWikiSearchCriteria() != null && !wikiSearchDto.getWikiSearchCriteria().isBlank()) {
                List<WikiResult> wikiSearchResult = wikiService.getSearchResult(wikiSearchDto.getWikiSearchCriteria());
                model.addAttribute("wikiSearchResult", wikiSearchResult);
            }
            return "single-lecture";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        } catch (URISyntaxException | IOException | InterruptedException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 500);
            return "5xx";
        }
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/solution")
    public String uploadSolution(@PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                                 @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                 @RequestParam("file") MultipartFile solution,
                                 Model model,
                                 HttpSession session) {
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            lectureService.uploadSolution(courseId, lectureId, loggedUser, solution);
            return "redirect:/course/{courseId}/lecture/{lectureId}";
        } catch (EntityNotFoundException | UnsupportedFileTypeException | StorageException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 400);
            return "4xx";
        }
    }

    @GetMapping("/{courseId}/lecture/{lectureId}/assignment")
    public ResponseEntity<Resource> downloadAssignment(@PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                                                       @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                                       HttpSession session) throws IOException {
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            Resource resource = lectureService.downloadAssignment(courseId, lectureId, loggedUser);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFile().getName());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{courseId}/lecture/{lectureId}/update")
    public String showLectureUpdatePage(@PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                                        @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int id,
                                        Model model,
                                        HttpSession session) {
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            Lecture lectureToUpdate = lectureService.get(courseId, id, loggedUser);
            UpdateLectureDto lectureDto = updateLectureDtoMapper.objectToDto(lectureToUpdate);
            model.addAttribute("lectureDto", lectureDto);
            return "update-lecture";
        } catch (AuthorizationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 401);
            return "4xx";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 404);
            return "4xx";
        }
    }

    @PostMapping("{courseId}/lecture/{lectureId}/update")
    public String update(HttpSession session,
                         @RequestPart @Valid @ModelAttribute("lectureDto") UpdateLectureDto lectureDto,
                         @PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer") int courseId,
                         @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                         Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            MultipartFile assignment = lectureDto.getAssignment();
            Lecture updateLecture = updateLectureDtoMapper.dtoToObject(lectureDto);
            updateLecture.setCourseId(courseId);
            updateLecture.setId(lectureId);
            if (lectureDto.getDescription() != null) {
                updateLecture.setDescription(lectureDto.getDescription());
            }
            lectureService.update(updateLecture, user, assignment);
            return String.format("redirect:/courses/%d", courseId);
        } catch (AuthorizationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 401);
            return "4xx";
        } catch (EntityDuplicateException | UnsupportedFileTypeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 422);
            return "4xx";
        }
    }

    @GetMapping("/{courseId}/lecture/{lectureId}/grade")
    public String showSolutionGradingPage(@PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                          Model model,
                                          HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("solutionList", userService.getStudentsByLectureId(lectureId));
            return "grade-assignment";
        } catch (AuthorizationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 401);
            return "4xx";
        }

    }

    @PostMapping("/{courseId}/lecture/{lectureId}/user/{userId}/grade")
    public String gradeAssignment(@PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                  @PathVariable(name = "userId") int userId,
                                  @PathVariable(name = "courseId") int courseId,
                                  @RequestParam("grade") int grade,
                                  HttpSession session,
                                  Model model) {
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            Solution solution = solutionDao.getSolution(userId, lectureId);
            solution.setGrade(grade);
            solutionService.addSolutionGrade(solution, loggedUser, courseId);
            return "redirect:/course/{courseId}/lecture/{lectureId}/grade";
        } catch (AuthorizationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", 401);
            return "4xx";
        }
    }


    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
}
