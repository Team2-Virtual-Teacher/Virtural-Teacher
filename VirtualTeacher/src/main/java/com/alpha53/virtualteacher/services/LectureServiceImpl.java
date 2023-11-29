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
import com.alpha53.virtualteacher.utilities.helpers.PermissionHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LectureServiceImpl implements LectureService {

    private static final String LECTURE_PERMIT_DELETE_EXCEPTION = "Only  creator of the course or admin can delete a lecture.";
    private static final String LECTURE_PERMIT_CREATE_EXCEPTION = "Only  creator of the course or admin can create a lecture.";
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

        List<Course> enrolledCourses = courseDao.getUsersEnrolledCourses(user.getUserId());

        if (enrolledCourses.stream().anyMatch(c -> c.getCourseId() == courseId)) {
            return lectureDao.get(lectureId);
        }
        throw new EntityNotFoundException("No lectures to show for user with ID: " + user.getUserId() + " in course with ID " + courseId);

    }

    //TODO Refactor error message in appropriate manner
    @Override
    public List<Lecture> getAllByCourseId(int courseId, User user) {

        Course course = courseDao.get(courseId);
        if (user.getRole().getRoleType().equalsIgnoreCase("admin") ||
                course.getCreator().getUserId() == user.getUserId()) {
            return lectureDao.getAllByCourseId(courseId);
        }

        List<Course> enrolledCourses = courseDao.getUsersEnrolledCourses(user.getUserId());
        if (enrolledCourses.stream().anyMatch(c -> c.getCourseId() == courseId)) {
            return lectureDao.getAllByCourseId(courseId);
        }
        throw new EntityNotFoundException("No lectures to show for user with ID: " + user.getUserId() + " in course with ID " + courseId);
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
        if (user.getRole().getRoleType().equalsIgnoreCase("admin") ||
                course.getCreator().getUserId() == user.getUserId()) {

            List<Lecture> lectureList = lectureDao.getAllByCourseId(lecture.getCourseId());
            boolean isTitleExist = lectureList.stream().anyMatch(l -> l.getTitle().equalsIgnoreCase(lecture.getTitle()));
            if (isTitleExist) {
                throw new EntityDuplicateException("Lecture", "title", lecture.getTitle());
            }
            lectureDao.create(lecture);
        } else {
            throw new AuthorizationException(LECTURE_PERMIT_CREATE_EXCEPTION);
        }

    }

    //TODO I can use course ID from controller
    @Override
    public void update(Lecture lecture, User user) {

        if (lectureDao.getCourseCreatorId(lecture.getId()) == user.getUserId()
                || user.getRole().getRoleType().equalsIgnoreCase("admin")) {

            List<Lecture> lectureList = lectureDao.getAllByCourseId(lecture.getCourseId());
            boolean isTitleExist = lectureList.stream().anyMatch(l -> l.getTitle().equalsIgnoreCase(lecture.getTitle()));
            if (isTitleExist) {
                throw new EntityDuplicateException("Lecture", "title", lecture.getTitle());
            }
            lectureDao.update(lecture);
        } else {
            throw new AuthorizationException(LECTURE_PERMIT_CREATE_EXCEPTION);
        }

    }

    //TODO Reformat error message
    // May be we should remove assignment files to this lecture into history directory and remove from main directory
    @Override
    public void delete(int courseId, int lectureId, User user) {
        Course course = courseDao.get(courseId);
        if (user.getRole().getRoleType().equalsIgnoreCase("admin") ||
                course.getCreator().getUserId() == user.getUserId()) {
           List<Solution> solutionList = solutionDao.getAllByLectureId(lectureId);

            if (lectureDao.delete(lectureId) == 0) {
                throw new EntityNotFoundException("Lecture", "id", String.valueOf(lectureId));
            }
            storageService.deleteAll(solutionList);

        } else {
            throw new AuthorizationException(LECTURE_PERMIT_DELETE_EXCEPTION);
        }
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

            Optional<String> result = solutionDao.getSolutionUrl(lectureId);

            String fileUrl = storageService.store(solution);

            if (result.isPresent()) {
                storageService.delete(result.get());
                solutionDao.updateSolution(user.getUserId(), lectureId, fileUrl);
            } else {

                solutionDao.addSolution(user.getUserId(), lectureId, fileUrl);
            }
        }

    }


}
