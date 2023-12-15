package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import com.alpha53.virtualteacher.services.contracts.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LectureServiceImplTests {
    @Mock
    private final LectureDao lectureDao;
    @Mock
    private final CourseDao courseDao;
    @Mock
    private final SolutionDao solutionDao;
    @Mock
    private final StorageService storageService;
    @InjectMocks
    private final LectureServiceImpl lectureService;

    public LectureServiceImplTests(LectureDao lectureDao,
                                   CourseDao courseDao,
                                   SolutionDao solutionDao,
                                   StorageService storageService, LectureServiceImpl lectureService) {
        this.lectureDao = lectureDao;
        this.courseDao = courseDao;
        this.solutionDao = solutionDao;
        this.storageService = storageService;
        this.lectureService = lectureService;
    }
//    Lecture get(int courseId, int lectureId, User user);
//
//    List<Lecture> getAllByCourseId(int courseId, User user);
//
//    void create(Lecture lecture, User user, MultipartFile assignment);
//
//
//    void update(Lecture lecture, User user, MultipartFile assignment);
//
//    void delete(int courseId, int lectureId, User user);
//
//    void uploadSolution(int courseId, int lectureId, User user, MultipartFile assignmentSolution);
//
//    boolean isAssignmentExist(int lectureId);

    @Test
    public void get_Should_Call_IfValid() {
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();


        Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);

        lectureService.get(mockCourse.getCourseId(),mockLecture.getId(),mockUser);


        Mockito.verify(lectureDao, Mockito.times(1)).get(Mockito.anyInt());
    }



    @Test
    public void get_Should_ThrowWhenUser_NotEnrolled() {
//        User mockUser = Helpers.createMockTeacher();
//        Course mockCourse = Helpers.createMockCourse();
//
//        mockUser.setRole(Helpers.createMockTeacherRole());
//
//        Mockito.when(courseDao.getByTitle(Mockito.anyString())).thenReturn(mockCourse);
//
//        Assertions.assertThrows(EntityDuplicateException.class, () -> courseService.create(mockCourse, mockUser));
    }



}
