package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;

import java.util.List;

public interface UserService {

    User get(int id);

    User get(String email);

    List<User> getAll();

    void create(User user, String userRole);

    void update(UserDto userDto, User user);

    void delete(int id, User user);
}
