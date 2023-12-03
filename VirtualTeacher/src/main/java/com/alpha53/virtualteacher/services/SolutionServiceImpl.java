package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Solution;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import com.alpha53.virtualteacher.services.contracts.SolutionService;
import org.springframework.stereotype.Service;

@Service
public class SolutionServiceImpl implements SolutionService {
    public static final String NOT_CREATOR_EXCEPTION = "Only creator of the course can add grade";
    public static final String STUDENT_NOT_ENROLLED_ERROR = "Student is not enrolled for the course";

    private final CourseDao courseDao;
    private final SolutionDao solutionDao;

    public SolutionServiceImpl( CourseDao courseDao, SolutionDao solutionDao) {
        this.courseDao = courseDao;
        this.solutionDao = solutionDao;
    }

    @Override
    //TODO check Async annotation how its work
    public void addSolutionGrade(Solution solution, User user, int courseId) {

        Course course = courseDao.get(courseId);
        checkCreator(user, course);
        if (!courseDao.isUserEnrolled(solution.getUserId(), courseId)) {
            throw new EntityNotFoundException(STUDENT_NOT_ENROLLED_ERROR);
        }
        Solution existingSolution = solutionDao.getSolution(solution.getUserId(), solution.getLectureId());
        existingSolution.setGrade(solution.getGrade());
        solutionDao.addGrade(existingSolution);

    }

    private void checkCreator(User user, Course course) {
        if (course.getCreator().getUserId() != user.getUserId()) {
            throw new AuthorizationException(NOT_CREATOR_EXCEPTION);
        }
    }


}
