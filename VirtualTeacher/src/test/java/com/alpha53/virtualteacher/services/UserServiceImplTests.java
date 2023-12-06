package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.exceptions.RegistrationException;
import com.alpha53.virtualteacher.models.*;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserDao userDao;

    @Mock
    CourseDao courseDao;

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


    @Test
    public void getById_Should_ThrowWhenUserDoesNotExist() {
        Mockito.when(userDao.get(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.get(Mockito.anyInt()));
    }

    @Test
    public void getById_Should_CallDaoWhenUserExists() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        List<Course> mockCourses = new ArrayList<>(List.of(mockCourse));

        Mockito.when(userDao.get(mockUser.getUserId())).thenReturn(mockUser);
        Mockito.when(courseDao.getCoursesByUser(mockUser.getUserId())).thenReturn(mockCourses);

        userService.get(mockUser.getUserId());

        Mockito.verify(userDao, Mockito.times(1)).get(mockUser.getUserId());
        Mockito.verify(courseDao, Mockito.times(1)).getCoursesByUser(mockUser.getUserId());
    }

    @Test
    public void getById_Should_SetCoursesWhenStudent() {
        User mockUser = Helpers.createMockStudent();
        Course mockCourse = Helpers.createMockCourse();
        List<Course> mockCourses = new ArrayList<>(List.of(mockCourse));

        Mockito.when(userDao.get(mockUser.getUserId())).thenReturn(mockUser);
        Mockito.when(courseDao.getCoursesByUser(mockUser.getUserId())).thenReturn(mockCourses);

        User user = userService.get(mockUser.getUserId());

        Assertions.assertEquals(user.getCourses().size(), 1);

        Mockito.verify(courseDao, Mockito.times(0)).getCoursesByCreator(Mockito.anyInt());
    }

    @Test
    public void getById_Should_SetCoursesWhenTeacher() {
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();
        mockCourse.setCreator(mockUser);
        List<Course> mockCourses = new ArrayList<>(List.of(mockCourse));

        Mockito.when(userDao.get(mockUser.getUserId())).thenReturn(mockUser);
        Mockito.when(courseDao.getCoursesByCreator(mockUser.getUserId())).thenReturn(mockCourses);

        User user = userService.get(mockUser.getUserId());

        Assertions.assertEquals(user.getCourses().size(), 1);
        Mockito.verify(courseDao, Mockito.times(0)).getCoursesByUser(Mockito.anyInt());
    }

    @Test
    public void getById_Should_SetCoursesWhenAdmin() {
        User mockUser = Helpers.createMockAdmin();
        Course mockCourse = Helpers.createMockCourse();
        mockCourse.setCreator(mockUser);
        List<Course> mockCourses = new ArrayList<>(List.of(mockCourse));

        Mockito.when(userDao.get(mockUser.getUserId())).thenReturn(mockUser);
        Mockito.when(courseDao.getCoursesByCreator(mockUser.getUserId())).thenReturn(mockCourses);

        User user = userService.get(mockUser.getUserId());

        Assertions.assertEquals(user.getCourses().size(), 1);
        Mockito.verify(courseDao, Mockito.times(0)).getCoursesByUser(Mockito.anyInt());
    }

    @Test
    public void getByEmail_Should_ThrowWhenUserDoesNotExist() {
        Mockito.when(userDao.get(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.get(Mockito.anyString()));
    }

    @Test
    public void getByEmail_Should_CallDaoWhenUserExists() {
        User mockUser = Helpers.createMockStudent();

        Mockito.when(userDao.get(mockUser.getEmail())).thenReturn(mockUser);

        userService.get(mockUser.getEmail());

        Mockito.verify(userDao, Mockito.times(1)).get(mockUser.getEmail());
    }

    @Test
    public void getAllUsers_Should_ReturnListOfUsers() {
        User mockStudent = Helpers.createMockStudent();
        User mockTeacher = Helpers.createMockTeacher();
        User mockAdmin = Helpers.createMockAdmin();
        List<User> mockList = List.of(mockStudent, mockTeacher, mockAdmin);
        FilterOptionsUsers mockUserFilter = new FilterOptionsUsers("mockemail@abv.bg",
                "Gosho",
                "Ivanov",
                "",
                "email",
                "");

        Mockito.when(userDao.getAll(mockUserFilter)).thenReturn(mockList);

        List<User> result = userService.getAll(mockUserFilter);

        Mockito.verify(userDao, Mockito.times(1)).getAll(mockUserFilter);

        Assertions.assertEquals(result.size(), 3);
    }

    @Test
    public void getAllUsers_Should_ThrowWhenInvalidRole() {
        FilterOptionsUsers mockUserFilter = new FilterOptionsUsers("mockemail@abv.bg",
                "Gosho",
                "Ivanov",
                "Alien",
                "email",
                "");

        Mockito.when(userDao.getRole(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getAll(mockUserFilter));

        Mockito.verify(userDao, Mockito.times(0)).getAll(mockUserFilter);
    }


    @Test
    public void create_Should_CallDaoWhenValidInput() {
        User mockUser = Helpers.createMockStudent();

        Mockito.when(userDao.get(Mockito.anyString())).thenThrow(EntityNotFoundException.class);
        Mockito.when(userDao.getRole(Mockito.anyString())).thenReturn(Helpers.createMockStudentRole());

        userService.create(mockUser, mockUser.getRole().getRoleType());

        Mockito.verify(userDao, Mockito.times(1)).create(mockUser);
    }

    @Test
    public void create_Should_CreateAndSentConfirmationTokenWhenValidInput() {
        User mockUser = Helpers.createMockStudent();

        Mockito.when(userDao.get(Mockito.anyString())).thenThrow(EntityNotFoundException.class);
        Mockito.when(userDao.getRole(Mockito.anyString())).thenReturn(Helpers.createMockStudentRole());
        Mockito.when(emailService.generateConfirmationEmail(Mockito.anyString(), Mockito.anyString())).thenReturn("test");

        userService.create(mockUser, mockUser.getRole().getRoleType());

        Mockito.verify(confirmationTokenService, Mockito.times(1)).save(Mockito.any(ConfirmationToken.class));
        Mockito.verify(emailService, Mockito.times(1)).generateConfirmationEmail(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(emailService, Mockito.times(1)).send(mockUser.getEmail(), "test", UserServiceImpl.REGISTRATION_CONFIRMATION_SUBJECT, null, null);

    }

    @Test
    public void create_Should_ThrowWhenEmailExistsAndUserIsVerified() {
        User mockUser = Helpers.createMockStudent();

        Mockito.when(userDao.get(Mockito.anyString())).thenReturn(mockUser);

        Assertions.assertThrows(EntityDuplicateException.class, () -> userService.create(mockUser, mockUser.getRole().getRoleType()));
    }

    @Test
    public void create_Should_CreateAndSendNewConfirmationToken_WhenEmailExistsAndUserIsNotVerified() {
        User mockUser = Helpers.createMockStudent();
        mockUser.setVerified(false);

        Mockito.when(userDao.get(Mockito.anyString())).thenReturn(mockUser);
        Mockito.when(emailService.generateConfirmationEmail(Mockito.anyString(), Mockito.anyString())).thenReturn("test");

        userService.create(mockUser, mockUser.getRole().getRoleType());


        Mockito.verify(confirmationTokenService, Mockito.times(1)).save(Mockito.any(ConfirmationToken.class));
        Mockito.verify(emailService, Mockito.times(1)).send(mockUser.getEmail(), "test", UserServiceImpl.REGISTRATION_CONFIRMATION_SUBJECT, null, null);

    }

    @Test
    public void create_Should_ThrowWhenInvalidRole() {
        User mockUser = Helpers.createMockStudent();

        Mockito.when(userDao.get(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.create(mockUser, "Admin"));

    }

    @Test
    public void create_Should_SetRoleToPendingTeacherWhenTeacherRoleIsPassed() {
        User mockUser = Helpers.createMockTeacher();

        Mockito.when(userDao.get(Mockito.anyString())).thenThrow(EntityNotFoundException.class);
        Mockito.when(userDao.getRole(Mockito.anyString())).thenReturn(Mockito.mock(Role.class));

        userService.create(mockUser, mockUser.getRole().getRoleType());

        Mockito.verify(userDao).getRole("PendingTeacher");
    }

    @Test
    public void confirmRegistration_Should_ThrowWhenInvalidToken() {
        Mockito.when(confirmationTokenService.get(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.confirmRegistration(Mockito.anyString()));
    }

    @Test
    public void confirmRegistration_Should_ThrowWhenRegistrationAlreadyConfirmed() {
        ConfirmationToken confirmationToken = new ConfirmationToken(Helpers.createMockStudent().getEmail());
        confirmationToken.setConfirmedAt(LocalDateTime.now());

        Mockito.when(confirmationTokenService.get(Mockito.anyString())).thenReturn(confirmationToken);

        Assertions.assertThrows(RegistrationException.class, () -> userService.confirmRegistration(Mockito.anyString()));
    }

    @Test
    public void confirmRegistration_Should_ThrowWhenTokenExpired() {
        ConfirmationToken confirmationToken = new ConfirmationToken(Helpers.createMockStudent().getEmail());
        confirmationToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        Mockito.when(confirmationTokenService.get(Mockito.anyString())).thenReturn(confirmationToken);

        Assertions.assertThrows(RegistrationException.class, () -> userService.confirmRegistration(Mockito.anyString()));
    }

    @Test
    public void confirmRegistration_Should_CallDaoWhenValidConfirmation() {
        User mockUser = Helpers.createMockStudent();
        ConfirmationToken confirmationToken = new ConfirmationToken(mockUser.getEmail());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        Mockito.when(confirmationTokenService.get(Mockito.anyString())).thenReturn(confirmationToken);
        Mockito.when(userDao.get(confirmationToken.getUserEmail())).thenReturn(mockUser);

        userService.confirmRegistration(confirmationToken.getToken());

        Mockito.verify(userDao, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void update_Should_ThrowWhenUserDoesNotMatch() {
        User mockLoggedUser = Helpers.createMockStudent();

        UserDto userDto = new UserDto();
        userDto.setEmail(mockLoggedUser.getEmail());
        userDto.setPassword(mockLoggedUser.getPassword());
        userDto.setFirstName("Gosho");
        userDto.setLastName("Peshov");
        userDto.setRole(mockLoggedUser.getRole().getRoleType());


        Assertions.assertThrows(AuthorizationException.class, () -> userService.update(userDto, mockLoggedUser, 2));
    }

    @Test
    public void update_Should_ThrowWhenUserAttemptsToChangeEmail() {
        User mockLoggedUser = Helpers.createMockStudent();
        UserDto userDto = new UserDto();
        userDto.setEmail("test@gmail.com");
        userDto.setPassword(mockLoggedUser.getPassword());
        userDto.setFirstName(mockLoggedUser.getFirstName());
        userDto.setLastName(mockLoggedUser.getFirstName());
        userDto.setRole(mockLoggedUser.getRole().getRoleType());

        Assertions.assertThrows(AuthorizationException.class, () -> userService.update(userDto, mockLoggedUser, mockLoggedUser.getUserId()));
    }

    @Test
    public void update_Should_ThrowWhenPendingTeacher() {
        User mockLoggedUser = Helpers.createMockPendingTeacher();
        UserDto userDto = new UserDto();
        userDto.setEmail(mockLoggedUser.getEmail());
        userDto.setPassword(mockLoggedUser.getPassword());
        userDto.setFirstName(mockLoggedUser.getFirstName());
        userDto.setLastName(mockLoggedUser.getFirstName());
        userDto.setRole(mockLoggedUser.getRole().getRoleType());

        Assertions.assertThrows(AuthorizationException.class, () -> userService.update(userDto, mockLoggedUser, mockLoggedUser.getUserId()));
    }

    @Test
    public void update_Should_ThrowWhenUserAttemptsToChangeRole() {
        User mockLoggedUser = Helpers.createMockStudent();
        UserDto userDto = new UserDto();
        userDto.setEmail(mockLoggedUser.getEmail());
        userDto.setPassword(mockLoggedUser.getPassword());
        userDto.setFirstName(mockLoggedUser.getFirstName());
        userDto.setLastName(mockLoggedUser.getFirstName());
        userDto.setRole("Admin");

        Assertions.assertThrows(AuthorizationException.class, () -> userService.update(userDto, mockLoggedUser, mockLoggedUser.getUserId()));
    }

    @Test
    public void update_Should_CallDaoWhenValidRequest() {
        User mockLoggedUser = Helpers.createMockStudent();
        UserDto userDto = new UserDto();
        userDto.setEmail(mockLoggedUser.getEmail());
        userDto.setPassword(mockLoggedUser.getPassword());
        userDto.setFirstName("Viktor");
        userDto.setLastName(mockLoggedUser.getFirstName());
        userDto.setRole(mockLoggedUser.getRole().getRoleType());

        userService.update(userDto, mockLoggedUser, mockLoggedUser.getUserId());

        Mockito.verify(userDao, Mockito.times(1)).update(Mockito.any(User.class));
    }

    @Test
    public void delete_Should_ThrowWhenLoggedUserDoesNotMatchAndIsNotAdmin() {
        User mockUser = Helpers.createMockStudent();

        Assertions.assertThrows(AuthorizationException.class, () -> userService.delete(2, mockUser));
    }

    @Test
    public void delete_Should_ThrowWhenAdminAttemptsToDeleteAdmin() {
        User mockUser = Helpers.createMockAdmin();
        User anotherMockUser = Helpers.createMockAdmin();
        anotherMockUser.setUserId(213);

        Mockito.when(userDao.get(mockUser.getUserId())).thenReturn(mockUser);

        Assertions.assertThrows(AuthorizationException.class, () -> userService.delete(mockUser.getUserId(), anotherMockUser));
    }

    @Test
    public void delete_Should_ThrowWhenUserHasCourses() {
        User mockUser = Helpers.createMockTeacher();
        Course mockCourse = Helpers.createMockCourse();

        Mockito.when(userDao.get(mockUser.getUserId())).thenReturn(mockUser);
        Mockito.when(courseDao.getCoursesByCreator(Mockito.anyInt())).thenReturn(List.of(mockCourse));

        Assertions.assertThrows(AuthorizationException.class, () -> userService.delete(mockUser.getUserId(), mockUser));
    }

    @Test
    public void delete_Should_CallDaoWhenValidRequest() {
        User mockUser = Helpers.createMockStudent();

        Mockito.when(userDao.get(mockUser.getUserId())).thenReturn(mockUser);

        userService.delete(mockUser.getUserId(), mockUser);

        Mockito.verify(storageService, Mockito.times(1)).deleteAll(Mockito.anyList());
        Mockito.verify(storageService, Mockito.times(1)).delete(Mockito.anyString());
        Mockito.verify(userDao, Mockito.times(1)).delete(mockUser.getUserId());

    }

    @Test
    public void setUserRole_Should_ThrowWhenLoggedUserIsNotAdmin() {
        User mockLoggedUser = Helpers.createMockStudent();
        User mockUser = Helpers.createMockStudent();
        mockUser.setUserId(2);

        Assertions.assertThrows(AuthorizationException.class, () -> userService.setUserRole(mockLoggedUser, mockUser, "Admin"));
    }

    @Test
    public void setUserRole_Should_ThrowWhenUserToGetRoleIsAdmin() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockUser = Helpers.createMockAdmin();
        mockUser.setUserId(2);

        Assertions.assertThrows(AuthorizationException.class, () -> userService.setUserRole(mockLoggedUser, mockUser, "Teacher"));
    }

    @Test
    public void setUserRole_Should_ReturnWhenRolesMatch() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockUser = Helpers.createMockStudent();
        mockUser.setUserId(2);

        userService.setUserRole(mockLoggedUser, mockUser, "Student");

        Mockito.verify(userDao, Mockito.times(0)).update(mockUser);
    }

    @Test
    public void setUserRole_Should_ThrowWhenNewRoleIsStudent() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockTeacher = Helpers.createMockTeacher();

        Assertions.assertThrows(AuthorizationException.class, () -> userService.setUserRole(mockLoggedUser, mockTeacher, "Student"));
    }

    @Test
    public void setUserRole_Should_ThrowWhenNewRoleIsPendingTeacher() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockTeacher = Helpers.createMockTeacher();

        Assertions.assertThrows(AuthorizationException.class, () -> userService.setUserRole(mockLoggedUser, mockTeacher, "PendingTeacher"));
    }

    @Test
    public void setUserRole_Should_ThrowWhenNewRoleIsTeacherAndCurrentIsNotPendingTeacher() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockStudent = Helpers.createMockStudent();

        Assertions.assertThrows(AuthorizationException.class, () -> userService.setUserRole(mockLoggedUser, mockStudent, "Teacher"));
    }

    @Test
    public void setUserRole_Should_ThrowWhenNewRoleIsAdminAndCurrentIsStudent() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockStudent = Helpers.createMockStudent();

        Assertions.assertThrows(AuthorizationException.class, () -> userService.setUserRole(mockLoggedUser, mockStudent, "Admin"));
    }

    @Test
    public void setUserRole_Should_ThrowWhenInvalidRoleType() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockStudent = Helpers.createMockStudent();

        Mockito.when(userDao.getRole(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.setUserRole(mockLoggedUser, mockStudent, "Alien"));

    }

    @Test
    public void setUserRole_Should_CallDaoWhenValidRequest() {
        User mockLoggedUser = Helpers.createMockAdmin();
        User mockTeacher = Helpers.createMockTeacher();

        Mockito.when(userDao.getRole(Mockito.anyString())).thenReturn(Helpers.createMockAdminRole());

        userService.setUserRole(mockLoggedUser, mockTeacher, "Admin");

        Mockito.verify(userDao, Mockito.times(1)).update(mockTeacher);
    }


    @Test
    public void referFriend_Should_ThrowWhenUserIsRegistered() {
        User mockLoggedUser = Helpers.createMockStudent();

        Mockito.when(userDao.emailExists(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(EntityDuplicateException.class, () -> userService.referFriend(mockLoggedUser, "test@abv.bg"));

    }

    @Test
    public void referFriend_Should_SendEmailWhenValidRequest() {
        User mockLoggedUser = Helpers.createMockStudent();
        Mockito.when(emailService.generateReferralEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("test");

        userService.referFriend(mockLoggedUser, "test@abv.bg");

        Mockito.verify(emailService, Mockito.times(1)).send("test@abv.bg", "test", UserServiceImpl.REFERRAL_SUBJECT, null, null);
    }

    @Test
    public void uploadProfilePicture_Should_ThrowWhenUserDoesNotMatch(){
        User mockUser = Helpers.createMockStudent();

        Assertions.assertThrows(AuthorizationException.class, ()-> userService.uploadProfilePicture(null,mockUser, 2));
    }

    @Test
    public void uploadProfilePicture_Should_ThrowWhenUserIsPendingTeacher(){
        User mockUser = Helpers.createMockPendingTeacher();

        Assertions.assertThrows(AuthorizationException.class, ()-> userService.uploadProfilePicture(null,mockUser, 1));
    }

    @Test
    public void uploadProfilePicture_Should_NotDeleteOldPhotoIfDefault(){
        User mockUser = Helpers.createMockStudent();

        userService.uploadProfilePicture(null,mockUser, 1);

        Mockito.verify(storageService,Mockito.times(0)).delete(mockUser.getPictureUrl());
    }

    @Test
    public void uploadProfilePicture_Should_CallDaoWhenValidRequest(){
        User mockUser = Helpers.createMockStudent();
        mockUser.setPictureUrl("/resources/fileStorage/notDefaultPhoto.png");
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);


        userService.uploadProfilePicture(mockFile,mockUser, 1);

        Mockito.verify(storageService,Mockito.times(1)).delete("/resources/fileStorage/notDefaultPhoto.png");
        Mockito.verify(storageService,Mockito.times(1)).store(mockFile);
        Mockito.verify(userDao,Mockito.times(1)).update(mockUser);

    }
}