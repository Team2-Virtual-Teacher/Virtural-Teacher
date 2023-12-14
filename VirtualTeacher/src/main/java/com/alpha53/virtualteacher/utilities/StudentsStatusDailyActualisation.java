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
import java.util.*;

@Component
@EnableScheduling
public class StudentsStatusDailyActualisation {
    public static final String SUCCESSFUL_GRADUATION_TITLE = "Successful graduation of course: %s";
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
    @Scheduled(cron = "30 23 20 * * *")
    private void informGraduatedStudents() {
        System.out.println("Certificates sent");
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
                        ByteArrayOutputStream certificate = CertificateGenerator.generateCertificate(user.getFirstName(), course.getKey().getTitle());
                        String graduationEmail = emailService.generateGraduationEmail(user.getFirstName(), course.getKey().getTitle());
                        emailService.send(user.getEmail(),
                                graduationEmail,
                                String.format(SUCCESSFUL_GRADUATION_TITLE, course.getKey().getTitle()),
                                certificate,
                                "Certificate.pdf");
                    }
                }
            }
        }
    }
}
