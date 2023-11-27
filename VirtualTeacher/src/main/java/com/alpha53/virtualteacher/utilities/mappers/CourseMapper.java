package com.alpha53.virtualteacher.utilities.mappers;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseMapper implements RowMapper<Course> {


    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        Topic topic = new Topic();
        User creator = new User();

        course.setCourseId(rs.getInt("id"));
        course.setTitle(rs.getString("title"));
        course.setStartingDate(rs.getDate("start_date").toLocalDate());
        course.setPublished(rs.getBoolean("is_published"));
        course.setPassingGrade(rs.getDouble("passing_grade"));
        //TODO course dto

        creator.setUserId(rs.getInt("creator_id"));
        creator.setEmail(rs.getString("email"));
        creator.setFirstName(rs.getString("first_name"));
        creator.setLastName(rs.getString("last_name"));
        creator.setPictureUrl(rs.getString("picture_url"));
        topic.setTopicId(rs.getInt("topic_id"));
        topic.setTopic(rs.getString("topic"));

        course.setCreator(creator);
        course.setTopic(topic);
        return course;
    }


}

