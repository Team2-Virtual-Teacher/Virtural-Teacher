package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {

    private static final String LECTURE_PERMIT_DELETE_EXCEPTION = "Only  creator of the course or admin can delete a lecture.";
    private static final String LECTURE_PERMIT_CREATE_EXCEPTION = "Only  creator of the course or admin can create a lecture.";
    private final LectureDao lectureDao;
    private final CourseDao courseDao;


    public LectureServiceImpl(LectureDao lectureDao, CourseDao courseDao) {
        this.lectureDao = lectureDao;
        this.courseDao = courseDao;
    }

    @Override
    public Lecture get(int courseId,int lectureId, User user) {
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
    public void create(Lecture lecture, User user) {
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
    public void delete(int lectureId, User user) {
        if (lectureDao.getCourseCreatorId(lectureId) == user.getUserId()
                || user.getRole().getRoleType().equalsIgnoreCase("admin")) {

            if (lectureDao.delete(lectureId) == 0) {
                throw new EntityNotFoundException("Lecture", "id", String.valueOf(lectureId));
            }

        } else {
            throw new AuthorizationException(LECTURE_PERMIT_DELETE_EXCEPTION);
        }
    }

}
