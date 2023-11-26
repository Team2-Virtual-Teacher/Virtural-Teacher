package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.FilterOptionsUsers;
import com.alpha53.virtualteacher.models.Role;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.repositories.contracts.UserDao;
import com.alpha53.virtualteacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String EMAIL_UPDATE_EXCEPTION = "Email cannot be updated.";
    public static final String ROLE_UPDATE_EXCEPTION = "Role cannot be updated.";
    public static final String DELETE_USER_EXCEPTION = "You are not authorized to delete this user.";
    public static final String DELETE_TEACHER_EXCEPTION = "Teachers cannot be deleted until all courses created by them are transferred.";
    public static final String PENDING_VALIDATION_EXCEPTION = "Your registration is being reviewed and currently you cannot update profile details.";

    public static final String CANNOT_CHANGE_ADMIN_ROLE_EXCEPTION = "Admins cannot change the role of other admins.";
    public static final String USERS_CANNOT_CHANGE_ROLES_EXCEPTION = "Only admins can change roles of other users";
    public static final String CHANGE_ROLE_EXCEPTION = "You are not allowed to change the role of a %s to a %s";
    public final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userRepository) {
        this.userDao = userRepository;
    }


    @Override
    public User get(int id) {
        return userDao.get(id);
    }

    @Override
    public User get(String email) {
        return userDao.get(email);
    }

    @Override
    public List<User> getAll(FilterOptionsUsers filterOptionsUsers) {
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
            throw new EntityNotFoundException(String.format("Role %s does not exist!", userRole));
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
    public void update(UserDto userDto, User user) {
        if (!user.getEmail().equals(userDto.getEmail())) {
            throw new AuthorizationException(EMAIL_UPDATE_EXCEPTION);
        }
        if (user.getRole().getRoleType().equalsIgnoreCase("PendingTeacher")) {
            throw new AuthorizationException(PENDING_VALIDATION_EXCEPTION);
        }
        if (!user.getRole().getRoleType().equals(userDto.getRole())) {
            throw new AuthorizationException(ROLE_UPDATE_EXCEPTION);
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setPictureUrl(userDto.getPictureUrl());
        userDao.update(user);
    }

    @Override
    public void delete(int id, User user) {
        if (user.getUserId() != id && !user.getRole().getRoleType().equalsIgnoreCase("Admin")) {
            throw new AuthorizationException(DELETE_USER_EXCEPTION);
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
}
