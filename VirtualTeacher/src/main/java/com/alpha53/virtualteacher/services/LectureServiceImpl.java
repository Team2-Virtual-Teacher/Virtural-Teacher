package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.Solution;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import com.alpha53.virtualteacher.services.contracts.StorageService;
import com.alpha53.virtualteacher.utilities.helpers.FileValidator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Service
public class LectureServiceImpl implements LectureService {

    private static final String LECTURE_PERMIT_DELETE_EXCEPTION = "Only  creator of the course or admin can delete a lecture.";
    private static final String LECTURE_PERMIT_CREATE_EXCEPTION = "Only  creator of the course or admin can create a lecture.";
    private static final String LECTURE_PERMIT_UPDATE_EXCEPTION = "Only  creator of the course or admin can update a lecture.";
    public static final String ASSIGNMENT_UPLOAD_ERROR = "Assignment upload is not allowed on public course";
    public static final String AUTHORIZED_DOWNLOAD_FILE_EXCEPTION = "You are not authorized to download this file";
    private final LectureDao lectureDao;
    private final CourseDao courseDao;
    private final SolutionDao solutionDao;
    private final StorageService storageService;


    public LectureServiceImpl(LectureDao lectureDao, CourseDao courseDao, StorageService storageService,
                              SolutionDao solutionDao) {
        this.lectureDao = lectureDao;
        this.courseDao = courseDao;
        this.solutionDao = solutionDao;
        this.storageService = storageService;
    }

    @Override
    public Lecture get(int courseId, int lectureId, User user) {
        Course course = courseDao.get(courseId);
        if (user.getRole().getRoleType().equalsIgnoreCase("admin") ||
                course.getCreator().getUserId() == user.getUserId()) {
            return lectureDao.get(lectureId);
        }

        if (courseDao.isUserEnrolled(user.getUserId(), courseId)) {
            return lectureDao.get(lectureId);
        }
        throw new EntityNotFoundException(String.format("No lectures to show for user with ID: %d in course with ID: %d", user.getUserId(), courseId));
    }

    @Override
    public List<Lecture> getAllByCourseId(int courseId, User user) {

        Course course = courseDao.get(courseId);
        if (user.getRole().getRoleType().equalsIgnoreCase("admin") ||
                course.getCreator().getUserId() == user.getUserId()) {
            return lectureDao.getAllByCourseId(courseId);
        }

        List<Course> enrolledCourses = courseDao.getCoursesByUser(user.getUserId());
        if (enrolledCourses.stream().anyMatch(c -> c.getCourseId() == courseId)) {
            return lectureDao.getAllByCourseId(courseId);
        }
        return Collections.emptyList();

    }

    /**
     * Create lecture to a course
     * User must be creator of the course or with role "ADMIN"
     *
     * @param lecture - Lecture to create
     * @param user    - logged user
     */
    @Override
    public void create(Lecture lecture, User user, MultipartFile assignment) {
        Course course = courseDao.get(lecture.getCourseId());
        verifyLectureModifyPermit(user, course, LECTURE_PERMIT_CREATE_EXCEPTION);
        checkLectureTitleExist(lecture);
        String fileUrl = storageService.store(assignment);
        lecture.setAssignmentUrl(fileUrl);
        lectureDao.create(lecture);

    }

    @Transactional
    @Override
    public void update(Lecture lecture, User user, MultipartFile assignment) {
        Course course = courseDao.get(lecture.getCourseId());
        verifyLectureModifyPermit(user, course, LECTURE_PERMIT_UPDATE_EXCEPTION);
        if (assignment != null){
            FileValidator.fileTypeValidator(assignment, "text");
        }
        Optional<String> existAssignmentUrl = lectureDao.getAssignmentUrl(lecture.getId());
        if (course.isPublished()) {
            if (assignment != null) {
                throw new AuthorizationException(ASSIGNMENT_UPLOAD_ERROR);
            } else {
                existAssignmentUrl.ifPresent(lecture::setAssignmentUrl);
                lectureDao.update(lecture);
            }
        } else {
            if (assignment != null){
                String fileUrl = storageService.store(assignment);
                lecture.setAssignmentUrl(fileUrl);
            }
            lectureDao.update(lecture);
            existAssignmentUrl.ifPresent(storageService::delete);
        }

    }

    @Override
    public void delete(int courseId, int lectureId, User user) {
        Course course = courseDao.get(courseId);
        verifyLectureModifyPermit(user, course, LECTURE_PERMIT_DELETE_EXCEPTION);
        List<Solution> solutionList = solutionDao.getAllByLectureId(lectureId);
        if (lectureDao.delete(lectureId) == 0) {
            throw new EntityNotFoundException("Lecture", "id", String.valueOf(lectureId));
        }
        storageService.deleteAll(solutionList);
    }

    @Override
    public void uploadSolution(int courseId, int lectureId, User user, MultipartFile solution) {

        FileValidator.fileTypeValidator(solution, "text");
        if (courseDao.isUserEnrolled(user.getUserId(), courseId)) {
            Course course = courseDao.get(courseId);
            Set<Lecture> lectures = new HashSet<>(lectureDao.getAllByCourseId(course.getCourseId()));
            course.setLectures(lectures);
            if (course.getLectures().stream().noneMatch(lecture -> lecture.getId() == lectureId)) {
                throw new EntityNotFoundException("Lecture", "ID", String.valueOf(lectureId));
            }
            Optional<String> existingSolutionUrl = solutionDao.getSolutionUrl(lectureId);
            String fileUrl = storageService.store(solution);
            if (existingSolutionUrl.isPresent()) {
                storageService.delete(existingSolutionUrl.get());
                solutionDao.updateSolutionUrl(user.getUserId(), lectureId, fileUrl);
            } else {
                solutionDao.addSolution(user.getUserId(), lectureId, fileUrl);
            }
        }
    }

    @Override
    public boolean isAssignmentExist(int lectureId) {
        return lectureDao.isAssignmentExist(lectureId);
    }

    @Override
    public Resource downloadAssignment(int courseId, int lectureId, User user) {
        if (!courseDao.isUserEnrolled(user.getUserId(), courseId)) {
            throw new AuthorizationException(AUTHORIZED_DOWNLOAD_FILE_EXCEPTION);
        }
        String assignmentUrl = lectureDao.getAssignmentUrl(lectureId).orElseThrow(() -> new EntityNotFoundException(lectureId));
        String fileName = extractFileName(assignmentUrl);
        Path assignmentFullUrlPath = storageService.loadAbsolutFilePath(fileName);
        Resource assignment = new FileSystemResource(assignmentFullUrlPath);
        if (assignment.exists()) {
            return assignment;
        }
        throw new EntityNotFoundException(lectureId);
    }

    @Override
    public Resource downloadSolution(String solutionUrl,int courseId,User user)  {

        Course course = courseDao.get(courseId);
        if (user.getUserId()==course.getCreator().getUserId() || user.getRole().getRoleType().equalsIgnoreCase("admin")){
            String fileName = extractFileName(solutionUrl);
            Path assignmentFullUrlPath = storageService.loadAbsolutFilePath(fileName);
            Resource assignment = new FileSystemResource(assignmentFullUrlPath);
            if (assignment.exists()) {
                return assignment;
            }
            throw new EntityNotFoundException();
        }
       throw new AuthorizationException(AUTHORIZED_DOWNLOAD_FILE_EXCEPTION);

    }

    private String extractFileName(String assignmentUrl) {
        int startIndex = assignmentUrl.lastIndexOf("\\");
        return assignmentUrl.substring(startIndex + 1);
    }

    private void checkLectureTitleExist(Lecture lecture) {
        List<Lecture> lectureList = lectureDao.getAllByCourseId(lecture.getCourseId());
        boolean isTitleExist = lectureList.stream().anyMatch(l -> l.getTitle().equalsIgnoreCase(lecture.getTitle()));
        if (isTitleExist) {
            throw new EntityDuplicateException("Lecture", "title", lecture.getTitle());
        }
    }

    private void verifyLectureModifyPermit(User user, Course course, String errorMessage) {

        if (!user.getRole().getRoleType().equalsIgnoreCase("admin") &&
                course.getCreator().getUserId() != user.getUserId()) {
            throw new AuthorizationException(errorMessage);
        }
    }
}
