package com.alpha53.virtualteacher.utilities.mappers;

import com.alpha53.virtualteacher.models.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingMapper implements RowMapper<Rating> {


    @Override
    public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {

        User user = new User();
        Rating rating = new Rating();



        user.setUserId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPictureUrl(rs.getString("picture_url"));

        rating.setRating(rs.getDouble("rating"));
        rating.setComment(rs.getString("comment"));
        rating.setRater(user);
        return rating;
    }


}

