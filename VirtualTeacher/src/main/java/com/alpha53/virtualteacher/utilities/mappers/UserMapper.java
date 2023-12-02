package com.alpha53.virtualteacher.utilities.mappers;

import com.alpha53.virtualteacher.models.Role;
import com.alpha53.virtualteacher.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleType(rs.getString("role"));

        User user = new User();
        user.setUserId(rs.getInt("userId"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPictureUrl(rs.getString("picture_url"));
        user.setVerified(rs.getBoolean("is_verified"));
        user.setRole(role);

        return user;
    }
}
