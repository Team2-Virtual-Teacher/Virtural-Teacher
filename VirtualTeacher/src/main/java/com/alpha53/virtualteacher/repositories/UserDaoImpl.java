package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    public UserDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

    }

    @Override
    public User get(int id) {
        String query = "SELECT c";
        return null;
    }

    @Override
    public User get(String email) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public boolean emailExists(String email){
        return false;
    }
    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(int id) {

    }
}
