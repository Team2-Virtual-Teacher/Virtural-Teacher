package com.alpha53.virtualteacher.utilities.mappers;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Topic;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper implements RowMapper<Course> {

    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        Topic topic = new Topic();
        course.setCourseId(rs.getInt("id"));
        course.setTitle(rs.getString("title"));
        course.setCreator(rs.getInt("creator_id"));
        course.setStartingDate(rs.getDate("start_date").toLocalDate());
        course.setPublished(rs.getBoolean("is_published"));
        course.setPassingGrade(rs.getDouble("passing_grade"));
        topic.setTopicId(rs.getInt("topic_id"));
        topic.setTopic(rs.getString("topic"));
        course.setTopic(topic);
        return course;
    }
}
