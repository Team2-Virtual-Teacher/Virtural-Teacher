package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.FilterOptionsUsers;
import com.alpha53.virtualteacher.models.Role;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.models.dtos.UserDtoOut;
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
    public final UserDao userRepository;

    @Autowired
    public UserServiceImpl(UserDao userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User get(int id) {
        return userRepository.get(id);
    }

    @Override
    public User get(String email) {
        return userRepository.get(email);
    }

    @Override
    public List<User> getAll(FilterOptionsUsers filterOptionsUsers) {
        if (filterOptionsUsers.getRoleType().isPresent()){
            userRepository.getRole(filterOptionsUsers.getRoleType().get());
        }
        return userRepository.getAll(filterOptionsUsers);
    }

    @Override
    public void create(User user, String userRole) {
        if (userRepository.emailExists(user.getEmail())) {
            throw new EntityDuplicateException("User", "Email", user.getEmail());
        }
        if (!userRole.equalsIgnoreCase("Teacher") && !userRole.equalsIgnoreCase("User")){
            throw new EntityNotFoundException(String.format("Role %s does not exist!", userRole));
        }
        if (userRole.equalsIgnoreCase("Teacher")) {
            userRole ="PendingTeacher";
            // TODO: 21.11.23 inform all admins with email
        }
        Role role = userRepository.getRole(userRole);
        user.setRole(role);
        userRepository.create(user);
    }

    @Override
    public void update(UserDto userDto, User user) {
        if (!user.getEmail().equals(userDto.getEmail())) {
            throw new AuthorizationException(EMAIL_UPDATE_EXCEPTION);
        }
        if (user.getRole().getRoleType().equalsIgnoreCase("PendingTeacher")){
            throw new AuthorizationException(PENDING_VALIDATION_EXCEPTION);
        }
        if (!user.getRole().getRoleType().equals(userDto.getRole())) {
            throw new AuthorizationException(ROLE_UPDATE_EXCEPTION);
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setPictureUrl(userDto.getPictureUrl());
        userRepository.update(user);
    }

    @Override
    public void delete(int id, User user) {
        if (user.getUserId() != id  && !user.getRole().getRoleType().equalsIgnoreCase("Admin")){
            throw new AuthorizationException(DELETE_USER_EXCEPTION);
        }
        User userToDelete = userRepository.get(id);
        if (userToDelete.getRole().getRoleType().equalsIgnoreCase("Teacher")){
            if (!userToDelete.getCourses().isEmpty()){
                throw new AuthorizationException(DELETE_TEACHER_EXCEPTION);
            }
        }
        userRepository.delete(id);
    }
}
