package com.alpha53.virtualteacher.utilities.mappers;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.CourseDescription;
import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;



@Component
public class CourseDescriptionMapper implements RowMapper<CourseDescription> {


    @Override
    public CourseDescription mapRow(ResultSet rs, int rowNum) throws SQLException {
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setCourseId(rs.getInt("course_id"));
        courseDescription.setDescription(rs.getString("description"));

        return courseDescription;
    }




}

