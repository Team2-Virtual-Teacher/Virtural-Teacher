package com.alpha53.virtualteacher.services;

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

    @Test
    public void get_Should_ReturnLecture_When_UserIsCreator(){
      /*  User teacherCreator = Helpers.createMockTeacher();
        Course course = Helpers.createMockCourse();
        Lecture lecture = Helpers.createMockLecture();

        Mockito.when(courseDao.get(course.getCourseId())).thenReturn(course);
        Mockito.when(lectureDao.get(lecture.getId())).thenReturn(lecture);

        Lecture result = lectureService.get(course.getCourseId(),lecture.getId(),teacherCreator);

        Mockito.verify(courseDao,Mockito.times(1)).get(course.getCourseId());
        Mockito.verify(lectureDao,Mockito.times(1)).get(course.getCourseId());
        Assertions.assertEquals(lecture,result);*/

    }



}
