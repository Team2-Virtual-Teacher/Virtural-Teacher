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
import com.alpha53.virtualteacher.services.contracts.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTests {



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

    @Mock
    StorageService storageService;

    @Mock
    SolutionDao solutionDao;

    @InjectMocks
    UserServiceImpl userService;
    @InjectMocks
    CourseServiceImpl courseService;






    @Test
    public void create_Should_CallDaoWhenValidInput() {
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();

        mockUser.setRole(Helpers.createMockTeacherRole());

        Mockito.when(courseDao.getByTitle(Mockito.anyString())).thenThrow(EntityNotFoundException.class);


        courseService.create(mockCourse, mockUser);

        Mockito.verify(courseDao, Mockito.times(1)).create(mockCourse);
    }



    @Test
    public void create_Should_ThrowWhenCourse_WithSameTitleExists() {
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();

        mockUser.setRole(Helpers.createMockTeacherRole());

        Mockito.when(courseDao.getByTitle(Mockito.anyString())).thenReturn(mockCourse);

        Assertions.assertThrows(EntityDuplicateException.class, () -> courseService.create(mockCourse, mockUser));
    }


    @Test
    public void create_Should_ThrowWhen_UserIsStudent() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();

        mockUser.setRole(Helpers.createMockStudentRole());

        Mockito.when(courseDao.getByTitle(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(AuthorizationException.class, () -> courseService.create(mockCourse, mockUser));
    }



    @Test
    public void update_Should_CallDaoWhenValidInput() {
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();

        mockUser.setRole(Helpers.createMockTeacherRole());

        Mockito.when(courseDao.getByTitle(Mockito.anyString())).thenThrow(EntityNotFoundException.class);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);

        courseService.update(mockCourse, mockUser);

        Mockito.verify(courseDao, Mockito.times(1)).update(mockCourse);
    }



    @Test
    public void update_Should_ThrowWhenCourse_WithSameTitleExists() {
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();
        Course mockCourse2 = Helpers.createMockCourse();
        mockCourse2.setCourseId(23);

        mockUser.setRole(Helpers.createMockTeacherRole());

        Mockito.when(courseDao.getByTitle(Mockito.anyString())).thenReturn(mockCourse2);


        Assertions.assertThrows(EntityDuplicateException.class, () -> courseService.update(mockCourse, mockUser));
    }


    @Test
    public void update_Should_ThrowWhen_UserDoesNotHave_ModifyPermissions() {
        User mockUser = Helpers.createMockTeacher();
        User mockUser2 = Helpers.createMockTeacher();
        mockUser2.setUserId(123);
        Course mockCourse = Helpers.createMockCourse();
        mockCourse.setCreator(mockUser2);


        Mockito.when(courseDao.getByTitle(Mockito.anyString())).thenThrow(EntityNotFoundException.class);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);

        Assertions.assertThrows(AuthorizationException.class, () -> courseService.update(mockCourse, mockUser));
    }






    @Test
    public void delete_Should_CallDaoWhenValidInput() {
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();

        mockUser.setRole(Helpers.createMockTeacherRole());


        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);


        courseService.delete(mockCourse.getCourseId(), mockUser);

        Mockito.verify(courseDao, Mockito.times(1)).delete(mockCourse.getCourseId());
    }




    @Test
    public void delete_Should_ThrowWhen_UserDoesNotHave_ModifyPermissions() {
        User mockUser = Helpers.createMockTeacher();
        User mockUser2 = Helpers.createMockTeacher();
        mockUser2.setUserId(123);
        Course mockCourse = Helpers.createMockCourse();
        mockCourse.setCreator(mockUser2);



        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);

        Assertions.assertThrows(AuthorizationException.class, () -> courseService.delete(mockCourse.getCourseId(), mockUser));
    }



    @Test
    public void delete_Should_ThrowWhen_CourseHasEnrolledStudents(){
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();
        mockCourse.setPublished(true);

        mockUser.setRole(Helpers.createMockTeacherRole());


        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(courseDao.getStudentsWhichAreEnrolledForCourse(Mockito.anyInt())).thenReturn(Arrays.asList(mockUser, mockUser, mockUser));

        Assertions.assertThrows(AuthorizationException.class, () -> courseService.delete(mockCourse.getCourseId(), mockUser));
    }


    @Test
    public void getCourseById_Should_CallDaoWhenValidInput() {
        Course mockCourse = Helpers.createMockCourse();
        mockCourse.setPublished(true);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        courseService.getCourseById(mockCourse.getCourseId());

        Mockito.verify(courseDao, Mockito.times(1)).get(mockCourse.getCourseId());
    }
    @Test
    public void getCourseById_Should_ThrowWhen_CourseIsPrivate() {
        Course mockCourse = Helpers.createMockCourse();
        mockCourse.setPublished(false);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Assertions.assertThrows(AuthorizationException.class, () -> courseService.getCourseById(mockCourse.getCourseId()));
    }


    @Test
    public void getCourseByIdAuth_Should_CallDaoWhenValidInput() {
        Course mockCourse = Helpers.createMockCourse();
        User mockUser =  Helpers.createMockTeacher();
        mockCourse.setPublished(true);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        courseService.getCourseByIdAuth(mockCourse.getCourseId(), mockUser);

        Mockito.verify(courseDao, Mockito.times(1)).get(mockCourse.getCourseId());
    }
    @Test
    public void getCourseByIdAuth_Should_ThrowWhen_CourseIsPrivate_And_UserIsStudent() {
        Course mockCourse = Helpers.createMockCourse();
        User mockUser =  Helpers.createMockStudent();
        mockCourse.setPublished(false);
        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Assertions.assertThrows(AuthorizationException.class, () -> courseService.getCourseByIdAuth(mockCourse.getCourseId(), mockUser));
    }


    @Test
    public void getPublic_Should_CallDaoWhenValidInput() {
        Course mockCourse = Helpers.createMockCourse();
        FilterOptions filterOptions = new FilterOptions();
        Mockito.when(courseDao.get(filterOptions)).thenReturn(Arrays.asList(mockCourse, mockCourse, mockCourse));
        courseService.getPublic(filterOptions);

        Mockito.verify(courseDao, Mockito.times(1)).get(filterOptions);
    }


    @Test
    public void get_Should_CallDaoWhenValidInput() {
        Course mockCourse = Helpers.createMockCourse();
        Optional<User> mockUser = Optional.of(Helpers.createMockTeacher());
        FilterOptions filterOptions = new FilterOptions();
        Mockito.when(courseDao.get(filterOptions)).thenReturn(Arrays.asList(mockCourse, mockCourse, mockCourse));
        courseService.get(filterOptions, mockUser);

        Mockito.verify(courseDao, Mockito.times(1)).get(filterOptions);
    }



    @Test
    public void getUsersEnrolledCourses_Should_CallDaoWhenValidInput(){
        Course mockCourse = Helpers.createMockCourse();
        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(Arrays.asList(mockCourse, mockCourse, mockCourse));
        courseService.getUsersEnrolledCourses(1);

        Mockito.verify(courseDao, Mockito.times(1)).getCoursesByUser(Mockito.anyInt());
    }


    @Test
    public void getUsersCompletedCourses_Should_CallDaoWhenValidInput(){
        Course mockCourse = Helpers.createMockCourse();
        Mockito.when(courseDao.getUsersCompletedCourses(Mockito.anyInt())).thenReturn(Arrays.asList(mockCourse, mockCourse, mockCourse));
        courseService.getUsersCompletedCourses(1);

        Mockito.verify(courseDao, Mockito.times(1)).getUsersCompletedCourses(Mockito.anyInt());
    }



    @Test
    public void isUserEnrolled_Should_CallDaoWhenValidInput(){
        Course mockCourse = Helpers.createMockCourse();
        User mockUser = Helpers.createMockStudent();
        Mockito.when(courseDao.isUserEnrolled(Mockito.anyInt(),Mockito.anyInt())).thenReturn(Boolean.TRUE);
        courseService.isUserEnrolled(mockUser.getUserId(), mockCourse.getCourseId());

        Mockito.verify(courseDao, Mockito.times(1)).isUserEnrolled(Mockito.anyInt(), Mockito.anyInt());
    }



    @Test
    public void hasUserPassedCourse_Should_CallDaoWhenValidInput(){
        Course mockCourse = Helpers.createMockCourse();
        User mockUser = Helpers.createMockStudent();
        Mockito.when(courseDao.hasUserPassedCourse(Mockito.anyInt(),Mockito.anyInt())).thenReturn(Boolean.TRUE);
        courseService.hasUserPassedCourse(mockUser.getUserId(), mockCourse.getCourseId());

        Mockito.verify(courseDao, Mockito.times(1)).hasUserPassedCourse(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void enrollUserForCourse_Should_ThrowWhen_UserIsAlreadyEnrolled(){
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();

        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(Arrays.asList(mockCourse, mockCourse, mockCourse));


        Assertions.assertThrows(EntityDuplicateException.class, () -> courseService.enrollUserForCourse(mockUser, mockCourse.getCourseId()));
    }
    @Test
    public void enrollUserForCourse_Should_ThrowWhen_UserIsNotStudent(){
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();
        Course mockCourse2 = Helpers.createMockCourse();
        mockCourse2.setCourseId(123);

        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(Arrays.asList(mockCourse2, mockCourse2, mockCourse2));


        Assertions.assertThrows(AuthorizationException.class, () -> courseService.enrollUserForCourse(mockUser, mockCourse.getCourseId()));
    }
    @Test
    public void enrollUserForCourse_Should_ThrowWhen_CourseHasNotStarted(){
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();
        Course mockCourse2 = Helpers.createMockCourse();
        mockCourse.setStartingDate(LocalDate.MAX);
        mockCourse2.setCourseId(123);

        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(Arrays.asList(mockCourse2, mockCourse2, mockCourse2));


        Assertions.assertThrows(AuthorizationException.class, () -> courseService.enrollUserForCourse(mockUser, mockCourse.getCourseId()));
    }
    @Test
    public void enrollUserForCourse_Should_ThrowWhen_CourseIsNotPublic(){
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();
        Course mockCourse2 = Helpers.createMockCourse();
        mockCourse.setPublished(false);
        mockCourse2.setCourseId(123);

        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(Arrays.asList(mockCourse2, mockCourse2, mockCourse2));


        Assertions.assertThrows(AuthorizationException.class, () -> courseService.enrollUserForCourse(mockUser, mockCourse.getCourseId()));
    }



    @Test
    public void enrollUserForCourse_Should_CallDaoWhenValidInput(){
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        Course mockCourse2 = Helpers.createMockCourse();
        mockCourse.setPublished(true);
        mockCourse2.setCourseId(123);

        Mockito.when(courseDao.get(Mockito.anyInt())).thenReturn(mockCourse);
        Mockito.when(courseDao.getCoursesByUser(Mockito.anyInt())).thenReturn(Arrays.asList(mockCourse2, mockCourse2, mockCourse2));
        courseService.enrollUserForCourse(mockUser, mockCourse.getCourseId());

        Mockito.verify(courseDao, Mockito.times(1)).enrollUserForCourse(Mockito.anyInt(), Mockito.anyInt());
    }



    @Test
    public void rateCourse_Should_ThrowWhen_StudentHaveNotPassedCourseYet(){
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        RatingDto mockRating = new RatingDto(
                "This course is the best!",
                4.3,
                1);

        Mockito.when(courseDao.hasUserPassedCourse(Mockito.anyInt(),Mockito.anyInt())).thenReturn(false);
        Assertions.assertThrows(AuthorizationException.class, () -> courseService.rateCourse(mockRating, mockUser.getUserId(), mockCourse.getCourseId()));
    }
    @Test
    public void rateCourse_Should_CallDaoWhenValidInput(){
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        RatingDto mockRating = new RatingDto(
                "This course is the best!",
                4.3,
                1);

        Mockito.when(courseDao.hasUserPassedCourse(Mockito.anyInt(),Mockito.anyInt())).thenReturn(true);
        courseService.rateCourse(mockRating, mockCourse.getCourseId(), mockUser.getUserId());

        Mockito.verify(courseDao, Mockito.times(1)).rateCourse(Mockito.any(RatingDto.class),Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void transferTeacherCourses_ShouldThrow_WhenCurrentUserIsNotAdmin(){
        User currentUser = Helpers.createMockTeacher();
        User fromUser = Helpers.createMockTeacher();
        User toUser = Helpers.createMockTeacher();
        toUser.setUserId(123);




        Assertions.assertThrows(AuthorizationException.class, () -> courseService.transferTeacherCourses(fromUser.getUserId(), toUser.getUserId(), currentUser));
    }
// TODO
//    @Test
//    public void transferTeacherCourses_ShouldThrow_WhenUserIsNotTeacher(){
//        User currentUser = Helpers.createMockAdmin();
//        User fromUser = Helpers.createMockStudent();
//        User toUser = Helpers.createMockTeacher();
//        toUser.setUserId(123);
//
//
//        Mockito.when(userDao.get(Mockito.anyInt())).thenReturn(fromUser);
//        Mockito.when(userService.get(Mockito.anyInt())).thenReturn(fromUser);
//        Assertions.assertThrows(UnsupportedOperationException.class, () -> courseService.transferTeacherCourses(fromUser.getUserId(), toUser.getUserId(), currentUser));
//    }
//    @Test
//    public void transferTeacherCourses_ShouldThrow_WhenUserIsNotAssignedToTeacher(){
//
//    }
//
//    @Test
//    public void transferTeacherCourses_Should_CallDaoWhenValidInput(){
//
//    }
}
