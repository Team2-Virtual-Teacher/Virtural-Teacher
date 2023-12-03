package com.alpha53.virtualteacher.utilities;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import com.alpha53.virtualteacher.services.contracts.EmailService;
import com.alpha53.virtualteacher.utilities.helpers.CertificateGenerator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Component
@EnableScheduling
public class StudentsStatusDailyActualisation {
    private final CourseDao courseDao;
    private final LectureDao lectureDao;
    private final SolutionDao solutionDao;
    private final EmailService emailService;

    public StudentsStatusDailyActualisation(CourseDao courseDao, LectureDao lectureDao, SolutionDao solutionDao, EmailService emailService) {
        this.courseDao = courseDao;
        this.lectureDao = lectureDao;
        this.solutionDao = solutionDao;
        this.emailService = emailService;
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

    @Scheduled(cron = "15 52 * * * *")
    private void informGraduatedStudents() {
        System.out.println("TEST");
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
                        try {
                            ByteArrayOutputStream certificate = CertificateGenerator.generateCertificate(user.getFirstName(), course.getKey().getTitle());
                            emailService.send(user.getEmail(),
                                    emailService.buildReferralEmail(user.getFirstName(),
                                            user.getLastName(), ""),
                                    "",
                                    certificate,
                                    "certificate");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

    }
}
