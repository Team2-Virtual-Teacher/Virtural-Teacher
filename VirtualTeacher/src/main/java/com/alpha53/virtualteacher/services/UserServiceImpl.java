package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.FilterOptionsUsers;
import com.alpha53.virtualteacher.models.Role;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.UserDao;
import com.alpha53.virtualteacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    public static final String EMAIL_UPDATE_EXCEPTION = "Email cannot be updated.";
    public static final String ROLE_UPDATE_EXCEPTION = "Role cannot be updated.";
    public static final String MODIFY_USER_EXCEPTION = "You are not authorized to %s this user.";
    public static final String DELETE_TEACHER_EXCEPTION = "Teachers cannot be deleted until all courses created by them are transferred.";
    public static final String PENDING_VALIDATION_EXCEPTION = "Your registration is being reviewed and currently you cannot update profile details.";

    public static final String CANNOT_CHANGE_ADMIN_ROLE_EXCEPTION = "Admins cannot change the role of other admins.";
    public static final String USERS_CANNOT_CHANGE_ROLES_EXCEPTION = "Only admins can change roles of other users";
    public static final String CHANGE_ROLE_EXCEPTION = "You are not allowed to change the role of a %s to a %s";
    public static final String INVALID_ROLE_EXCEPTION = "%s is not a valid role!";
    private final UserDao userDao;
    private final CourseDao courseDao;

    @Autowired
    public UserServiceImpl(UserDao userRepository, CourseDao courseDao) {
        this.userDao = userRepository;
        this.courseDao = courseDao;
    }


    @Override
    public User get(int id) {
        User user = userDao.get(id);
        Set<Course> courseSet = new HashSet<>(courseDao.getCoursesByUser(id));
        user.setCourses(courseSet);
        return user;
    }

    @Override
    public User get(String email) {
        return userDao.get(email);
    }

    @Override
    public List<User> getAll(FilterOptionsUsers filterOptionsUsers) {
        //the following if-statement checks if the role passed (in case such exists) is a valid one. getRole throws
        //in case of invalid role/
        if (filterOptionsUsers.getRoleType().isPresent()) {
            userDao.getRole(filterOptionsUsers.getRoleType().get());
        }
        return userDao.getAll(filterOptionsUsers);
    }

    @Override
    public void create(User user, String userRole) {
        if (userDao.emailExists(user.getEmail())) {
            throw new EntityDuplicateException("User", "Email", user.getEmail());
        }
        if (!userRole.equalsIgnoreCase("Teacher") && !userRole.equalsIgnoreCase("User")) {
            throw new EntityNotFoundException(String.format(INVALID_ROLE_EXCEPTION, userRole));
        }
        if (userRole.equalsIgnoreCase("Teacher")) {
            userRole = "PendingTeacher";
            // TODO: 21.11.23 inform all admins with email
        }
        Role role = userDao.getRole(userRole);
        user.setRole(role);
        userDao.create(user);
    }

    @Override
    public void update(UserDto userDto, User user, int id) {

        if (user.getUserId() != id) {
            throw new AuthorizationException(String.format(MODIFY_USER_EXCEPTION, "update"));
        }
        if (!user.getEmail().equals(userDto.getEmail())) {
            throw new AuthorizationException(EMAIL_UPDATE_EXCEPTION);
        }
        if (user.getRole().getRoleType().equalsIgnoreCase("PendingTeacher")) {
            throw new AuthorizationException(PENDING_VALIDATION_EXCEPTION);
        }
        if (!user.getRole().getRoleType().equalsIgnoreCase(userDto.getRole())) {
            throw new AuthorizationException(ROLE_UPDATE_EXCEPTION);
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        userDao.update(user);
    }

    @Override
    public void delete(int id, User user) {
        if (user.getUserId() != id && !user.getRole().getRoleType().equalsIgnoreCase("Admin")) {
            throw new AuthorizationException(String.format(MODIFY_USER_EXCEPTION, "delete"));
        }
        User userToDelete = userDao.get(id);
        if (userToDelete.getRole().getRoleType().equalsIgnoreCase("Teacher")) {
            if (!userToDelete.getCourses().isEmpty()) {
                throw new AuthorizationException(DELETE_TEACHER_EXCEPTION);
            }
        }
        userDao.delete(id);
    }

    @Override
    public void setUserRole(User loggedInUser, User userToGetRole, String roleName) {
        if (!loggedInUser.getRole().getRoleType().equalsIgnoreCase("Admin")) {
            throw new AuthorizationException(USERS_CANNOT_CHANGE_ROLES_EXCEPTION);
        }
        if (userToGetRole.getRole().getRoleType().equalsIgnoreCase("Admin")) {
            throw new AuthorizationException(CANNOT_CHANGE_ADMIN_ROLE_EXCEPTION);
        }
        if (userToGetRole.getRole().getRoleType().equalsIgnoreCase(roleName)) {
            return;
        }
        if (roleName.equalsIgnoreCase("Student") || roleName.equalsIgnoreCase("PendingTeacher") ||
                (roleName.equalsIgnoreCase("Teacher")
                        && !userToGetRole.getRole().getRoleType().equalsIgnoreCase("PendingTeacher"))) {
            throw new AuthorizationException(String.format(CHANGE_ROLE_EXCEPTION, userToGetRole.getRole().getRoleType(), roleName));
        }

        Role roleToGive = userDao.getRole(roleName);

        userToGetRole.setRole(roleToGive);
        userDao.update(userToGetRole);
    }

    @Override
    public void uploadProfilePicture(String picturePath, User user, int id) {
        if (user.getUserId() != id) {
            throw new AuthorizationException(String.format(MODIFY_USER_EXCEPTION, "upload a profile picture for"));
        }
        if (user.getRole().getRoleType().equalsIgnoreCase("PendingTeacher")) {
            throw new AuthorizationException(PENDING_VALIDATION_EXCEPTION);
        }
        user.setPictureUrl(picturePath);
        userDao.update(user);
    }
}
