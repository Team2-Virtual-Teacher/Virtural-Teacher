package com.alpha53.virtualteacher.utilities.mappers.dtoMappers;

import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapperHelper {

    public User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPictureUrl(userDto.getPictureUrl());
        return user;
    }
}
