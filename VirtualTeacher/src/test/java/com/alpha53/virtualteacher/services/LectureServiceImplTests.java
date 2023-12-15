package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.*;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import com.alpha53.virtualteacher.repositories.contracts.UserDao;
import com.alpha53.virtualteacher.services.contracts.ConfirmationTokenService;
import com.alpha53.virtualteacher.services.contracts.EmailService;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import com.alpha53.virtualteacher.services.contracts.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class LectureServiceImplTests {



//    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId, User loggedUser);



    @Mock
    UserDao userDao;

    @Mock
    CourseDao courseDao;
    @Mock
    LectureDao lectureDao;

    @Mock
    ConfirmationTokenService confirmationTokenService;

    @Mock
    EmailService emailService;
    @InjectMocks
    LectureServiceImpl lectureService;

    @Mock
    StorageService storageService;

    @Mock
    SolutionDao solutionDao;

    @InjectMocks
    UserServiceImpl userService;
    @InjectMocks
    CourseServiceImpl courseService;






//    boolean isAssignmentExist(int lectureId);
//
//    Resource downloadAssignment(int courseId, int lectureId, User user) throws IOException;
    @Test
    public void get_Should_CallRepository_IfUserIsAdmin() {
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();


        Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        lectureService.get(mockCourse.getCourseId(),mockLecture.getId(),mockUser);


        Mockito.verify(lectureDao, Mockito.times(1)).get(Mockito.anyInt());
    }
    @Test
    public void get_Should_CallRepository_IfUserIsEnrolled() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();



        Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        lectureService.get(mockCourse.getCourseId(),mockLecture.getId(),mockUser);


        Mockito.verify(lectureDao, Mockito.times(1)).get(Mockito.anyInt());
    }

    @Test
    public void get_Should_ThrowWhenUser_NotEnrolled() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();
        mockUser.setUserId(123);


       // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(EntityNotFoundException.class, () -> lectureService.get(mockCourse.getCourseId(), mockLecture.getId(), mockUser));
    }


    @Test
    public void getAllByCourseId_Should_CallRepository_IfUserIsAdmin() {
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();


       // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        lectureService.getAllByCourseId(mockCourse.getCourseId(),mockUser);


        Mockito.verify(lectureDao, Mockito.times(1)).getAllByCourseId(Mockito.anyInt());
    }
    @Test
    public void getAllByCourseId_Should_CallRepository_IfUserIsEnrolled() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();
        mockUser.setUserId(123);



        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        List<Course> listOfCourses = new ArrayList<>();
        listOfCourses.add(mockCourse);
        listOfCourses.add(mockCourse);
        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(listOfCourses);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        lectureService.getAllByCourseId(mockCourse.getCourseId(),mockUser);


        Mockito.verify(lectureDao, Mockito.times(1)).getAllByCourseId(Mockito.anyInt());
    }

    @Test
    public void getAllByCourseId_Should_NotCallRepository_NotEnrolled() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();
        mockUser.setUserId(123);



        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        List<Course> listOfCourses = new ArrayList<>();

        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(listOfCourses);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        lectureService.getAllByCourseId(mockCourse.getCourseId(),mockUser);


        Mockito.verify(lectureDao, Mockito.times(0)).getAllByCourseId(Mockito.anyInt());
    }



    @Test
    public void create_ShouldCallRepository_IfValid(){
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();


        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        lectureService.create(mockLecture, mockUser, mock(MultipartFile.class));


        Mockito.verify(lectureDao, Mockito.times(1)).create(Mockito.any(Lecture.class));
    }
    @Test
    public void create_ShouldThrow_IfUserIsStudent(){
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();
        mockUser.setUserId(123);

        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);


        Assertions.assertThrows(AuthorizationException.class, () -> lectureService.create(mockLecture, mockUser, mock(MultipartFile.class)));

    }

    @Test
    public void update_ShouldCallRepository_IfValid(){
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();


        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);
        MultipartFile file = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());
        lectureService.update(mockLecture, mockUser, file);


        Mockito.verify(lectureDao, Mockito.times(1)).update(Mockito.any(Lecture.class));
    }


    @Test
    public void update_ShouldThrow_IfUserIsStudent(){
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();
        mockUser.setUserId(123);

        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);
        MultipartFile file = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());



        Assertions.assertThrows(AuthorizationException.class, () -> lectureService.update(mockLecture, mockUser, file));
    }

    @Test
    public void update_ShouldThrow_IfCourseIsPublic(){
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();
        mockCourse.setPublished(true);

        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);
        MultipartFile file = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());



        Assertions.assertThrows(AuthorizationException.class, () -> lectureService.update(mockLecture, mockUser, file));
    }
    @Test
    public void delete_ShouldCallRepository_IfValid(){
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();


        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        Mockito.when(solutionDao.getAllByLectureId(Mockito.anyInt())).thenReturn(new ArrayList<>());
        Mockito.when(lectureDao.delete(Mockito.anyInt())).thenReturn(1);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        lectureService.delete(mockCourse.getCourseId(), mockLecture.getId(), mockUser);


        Mockito.verify(lectureDao, Mockito.times(1)).delete(Mockito.anyInt());
    }
    @Test
    public void delete_ShouldThrow_IfLectureNotFound(){
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();


        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        Mockito.when(solutionDao.getAllByLectureId(Mockito.anyInt())).thenReturn(new ArrayList<>());
        Mockito.when(lectureDao.delete(Mockito.anyInt())).thenReturn(0);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);




        Assertions.assertThrows(EntityNotFoundException.class, () -> lectureService.delete(mockCourse.getCourseId(), mockLecture.getId(), mockUser));

    }

    @Test
    public void delete_ShouldThrow_IfUserIsStudent(){
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();
        mockUser.setUserId(123);

        // Mockito.when(lectureDao.get(Mockito.anyInt())).thenReturn(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        //Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        //Mockito.when(solutionDao.getAllByLectureId(Mockito.anyInt())).thenReturn(new ArrayList<>());
        //Mockito.when(lectureDao.delete(Mockito.anyInt())).thenReturn(1);
        //Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);




        Assertions.assertThrows(AuthorizationException.class, () -> lectureService.delete(mockCourse.getCourseId(), mockLecture.getId(), mockUser));

    }


    @Test
    public void uploadSolution_ShouldThrow_WhenLectureNotFound() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();

        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(lectureDao.getAllByCourseId(Mockito.anyInt())).thenReturn(new ArrayList<>());
      //  Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);
        MultipartFile file = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());

        Assertions.assertThrows(EntityNotFoundException.class, () -> lectureService.uploadSolution(mockCourse.getCourseId(), mockLecture.getId(), mockUser, file));
    }

    @Test
    public void uploadSolution_ShouldCallRepository_WhenSolutionPresent() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();

        List<Lecture> listOfCourses = new ArrayList<>();
        listOfCourses.add(mockLecture);
        listOfCourses.add(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(lectureDao.getAllByCourseId(Mockito.anyInt())).thenReturn(listOfCourses);
        Mockito.when(solutionDao.getSolutionUrl(Mockito.anyInt())).thenReturn(Optional.of("asd"));
        Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);
        MultipartFile file = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());

        lectureService.uploadSolution(mockCourse.getCourseId(), mockLecture.getId(), mockUser, file);
        Mockito.verify(solutionDao, Mockito.times(1)).updateSolutionUrl(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
    }
    @Test
    public void uploadSolution_ShouldCallRepository_WhenSolutionNotPresent() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();

        List<Lecture> listOfCourses = new ArrayList<>();
        listOfCourses.add(mockLecture);
        listOfCourses.add(mockLecture);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(lectureDao.getAllByCourseId(Mockito.anyInt())).thenReturn(listOfCourses);
        Mockito.when(solutionDao.getSolutionUrl(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(storageService.store(Mockito.any(MultipartFile.class))).thenReturn("asd");
        Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Boolean.TRUE);
        MultipartFile file = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());

        lectureService.uploadSolution(mockCourse.getCourseId(), mockLecture.getId(), mockUser, file);
        Mockito.verify(solutionDao, Mockito.times(1)).addSolution(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
    }


    @Test
    public void isAssignmentExist_ShouldCallRepository() {

        lectureService.isAssignmentExist(1);
        Mockito.verify(lectureDao, Mockito.times(1)).isAssignmentExist(Mockito.anyInt());
    }
}
