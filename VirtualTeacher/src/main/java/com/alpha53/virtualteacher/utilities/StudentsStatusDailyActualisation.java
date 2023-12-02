package com.alpha53.virtualteacher.utilities;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@EnableScheduling
public class StudentsStatusDailyActualisation {
    private final CourseDao courseDao;
    private final LectureDao lectureDao;
    private final SolutionDao solutionDao;
    public StudentsStatusDailyActualisation(CourseDao courseDao, LectureDao lectureDao, SolutionDao solutionDao) {
        this.courseDao = courseDao;
        this.lectureDao = lectureDao;
        this.solutionDao = solutionDao;
    }

   /* @Scheduled(cron = "22 42 * * * *")
    private void informCompleteOrFailCourse() {

        List<Integer> ongoingCoursesIds = courseDao.getIdOngoingCourses();
        Map<Course, List<User>> enrolledStudents = new HashMap<>();

        for (Integer ongoingCourse : ongoingCoursesIds) {
            Course course = courseDao.get(ongoingCourse);
            Set<Lecture> lectures = new HashSet<>(lectureDao.getAllByCourseId(ongoingCourse));
            course.setLectures(lectures);
            enrolledStudents.put(course, courseDao.getStudentsWhichAreEnrolledForCourse(ongoingCourse));
        }

        for (Map.Entry<Course, List<User>> course : enrolledStudents.entrySet()) {
            List<Integer> lectures = new ArrayList<>();
            course.getKey().getLectures().forEach(lecture -> lectures.add(lecture.getId()));
            for (User user : course.getValue()) {
                Map<Integer, Double> studentStatus = solutionDao.getSolutionCountAndAVGPerStudent(user.getUserId(), lectures);
                for (Map.Entry<Integer, Double> entry : studentStatus.entrySet()) {
                    if (entry.getKey() == course.getKey().getLectures().size() && entry.getValue() >= course.getKey().getPassingGrade()) {
                        courseDao.completeCourse(user.getUserId(),course.getKey().getCourseId());
                    }
                }
            }
        }
    }*/

    @Scheduled(cron = "01 41 * * * *")
    private void informGraduatedStudents() {

        List<Integer> ongoingCoursesIds = courseDao.getIdOngoingCourses();
        Map<Course, List<User>> enrolledStudents = new HashMap<>();

        for (Integer ongoingCourse : ongoingCoursesIds) {
            Course course = courseDao.get(ongoingCourse);
            Set<Lecture> lectures = new HashSet<>(lectureDao.getAllByCourseId(ongoingCourse));
            course.setLectures(lectures);
            enrolledStudents.put(course, courseDao.getStudentsWhichAreEnrolledForCourse(ongoingCourse));
        }

        for (Map.Entry<Course, List<User>> course : enrolledStudents.entrySet()) {
            for (User user : course.getValue()) {
                Map<Integer, Double> map = solutionDao.getSolutionCountAndAVGPerStudent(user.getUserId(), course.getKey().getCourseId());
                for (Map.Entry<Integer, Double> entry : map.entrySet()) {
                    if (entry.getKey() == course.getKey().getLectures().size() && entry.getValue() >= course.getKey().getPassingGrade()) {
                        courseDao.completeCourse(user.getUserId(), course.getKey().getCourseId());
                    }
                }
            }
        }

    }
}
