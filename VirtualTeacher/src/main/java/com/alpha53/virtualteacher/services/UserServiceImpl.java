package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.UserRoles;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String EMAIL_UPDATE_EXCEPTION = "Email cannot be updated!";
    public static final String ROLE_UPDATE_EXCEPTION = "Role cannot be updated!";
    public static final String DELETE_USER_EXCEPTION = "You are not authorized to delete this user!";
    public final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
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
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void create(User user) {
        if (userRepository.emailExists(user.getEmail())) {
            throw new EntityDuplicateException("User", "Email", user.getEmail());
        }
        if (user.getRole().getRole().toString().equalsIgnoreCase("Teacher")) {
            user.getRole().setRole(UserRoles.PENDING_TEACHER);
            // TODO: 21.11.23 inform all admins with email
        }
        userRepository.create(user);
    }

    @Override
    public void update(UserDto userDto, User user) {
        if (!user.getEmail().equals(userDto.getEmail())) {
            throw new AuthorizationException(EMAIL_UPDATE_EXCEPTION);
        }
        if (!user.getRole().getRole().toString().equals(userDto.getRole())) {
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
        if (user.getId() != id){
            throw new AuthorizationException(DELETE_USER_EXCEPTION);
        }
        userRepository.delete(id);
    }
}
