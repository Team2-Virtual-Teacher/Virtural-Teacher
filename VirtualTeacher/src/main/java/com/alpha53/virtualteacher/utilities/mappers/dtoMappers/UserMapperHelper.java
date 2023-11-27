package com.alpha53.virtualteacher.utilities.mappers.dtoMappers;

import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.models.dtos.UserDtoOut;
import org.springframework.stereotype.Component;

@Component
public class UserMapperHelper {

    public User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        return user;
    }

    public UserDtoOut userToUserDtoOut(User user) {
        UserDtoOut userDtoOut = new UserDtoOut();
        userDtoOut.setUserId(user.getUserId());
        userDtoOut.setEmail(user.getEmail());
        userDtoOut.setFirstName(user.getFirstName());
        userDtoOut.setLastName(user.getLastName());
        userDtoOut.setRole(user.getRole());
        userDtoOut.setPictureUrl(user.getPictureUrl());
        userDtoOut.setCourses(user.getCourses());

        return userDtoOut;
    }
}
