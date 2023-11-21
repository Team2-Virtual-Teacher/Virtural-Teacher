package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.User;

import java.util.List;

public interface UserDao {

    User get(int id);

    User get(String email);

    List<User> getAll();

    void create(User user);

    void update(User user);

    void delete(int id);
}
