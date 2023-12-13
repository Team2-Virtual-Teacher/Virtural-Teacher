package com.alpha53.virtualteacher.controllers.mvc;

import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.exceptions.StorageException;
import com.alpha53.virtualteacher.exceptions.UnsupportedFileTypeException;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.WikiResult;
import com.alpha53.virtualteacher.models.dtos.WikiSearchDto;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import com.alpha53.virtualteacher.services.contracts.WikiService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/course")
public class LectureMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final LectureService lectureService;
    private final WikiService wikiService;
    public LectureMvcController(AuthenticationHelper authenticationHelper, LectureService lectureService, WikiService wikiService) {
        this.authenticationHelper = authenticationHelper;
        this.lectureService = lectureService;
        this.wikiService = wikiService;
    }

    @GetMapping("/{courseId}/lecture/{lectureId}")
    public String singleLecture(@PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer")  int courseId,
                                @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                @ModelAttribute(name = "wikiSearchCriteria") WikiSearchDto wikiSearchDto,
                                Model model,
                                HttpSession session)  {

        try{
            User user = authenticationHelper.tryGetCurrentUser(session);
            Lecture lecture = lectureService.get(courseId,lectureId,user);
            model.addAttribute("lecture",lecture);
            model.addAttribute("isAssignmentExist",lectureService.isAssignmentExist(lectureId));
            if (wikiSearchDto.getWikiSearchCriteria() != null && !wikiSearchDto.getWikiSearchCriteria().isBlank()){
              List<WikiResult> wikiSearchResult = wikiService.getSearchResult(wikiSearchDto.getWikiSearchCriteria());
              model.addAttribute("wikiSearchResult",wikiSearchResult);
            }
            return "single-lecture";

        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage",e.getMessage());
            model.addAttribute("statusCode",404);
            return "4xx";
        } catch (URISyntaxException | IOException | InterruptedException e) {
            model.addAttribute("errorMessage",e.getMessage());
            model.addAttribute("statusCode",500);
            return "5xx";
        }

    }

    @PostMapping("/{courseId}/lecture/{lectureId}/solution")
    public String uploadSolution(@PathVariable(name = "courseId") @Positive(message = "Course ID must be a positive integer")  int courseId,
                                 @PathVariable(name = "lectureId") @Positive(message = "Lecture ID must be a positive integer") int lectureId,
                                 @RequestParam("file") MultipartFile solution,
                                 Model model,
                                 HttpSession session){
        try{
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            lectureService.uploadSolution(courseId,lectureId,loggedUser,solution);
            return "redirect:/course/{courseId}/lecture/{lectureId}";
        } catch (EntityNotFoundException | UnsupportedFileTypeException | StorageException e) {
            model.addAttribute("errorMessage",e.getMessage());
            model.addAttribute("statusCode",400);
            return "4xx";
        }
    }
}
