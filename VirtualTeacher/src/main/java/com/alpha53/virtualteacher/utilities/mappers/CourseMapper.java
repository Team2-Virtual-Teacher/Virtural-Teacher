package com.alpha53.virtualteacher.utilities.mappers;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.CourseDto;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.TopicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class CourseMapper implements RowMapper<Course> {

    private final TopicDao topicDao;
    private final CourseDao courseDao;

    @Autowired
    public CourseMapper(TopicDao topicDao, CourseDao courseDao) {
        this.topicDao = topicDao;
        this.courseDao = courseDao;
    }

    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        Topic topic = new Topic();
        User creator = new User();
        course.setCourseId(rs.getInt("id"));
        course.setTitle(rs.getString("title"));
        //TODO course dto
        creator.setUserId(rs.getInt("creator_id"));
        creator.setEmail(rs.getString("email"));
        creator.setFirstName(rs.getString("first_name"));
        creator.setLastName(rs.getString("last_name"));
        creator.setPictureUrl(rs.getString("profile_picture"));
        course.setStartingDate(rs.getDate("start_date").toLocalDate());
        course.setPublished(rs.getBoolean("is_published"));
        course.setPassingGrade(rs.getDouble("passing_grade"));
        topic.setTopicId(rs.getInt("topic_id"));
        topic.setTopic(rs.getString("topic"));
        course.setCreator(creator);
        course.setTopic(topic);
        return course;
    }

    public Course fromDto(CourseDto dto){
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setStartingDate(LocalDate.parse(dto.getStartingDate()));
        course.setPassingGrade(dto.getPassingGrade());
        course.setTopic(topicDao.getById(dto.getTopicId()));
        course.setPublished(dto.isPublished());
        course.setPassingGrade(dto.getPassingGrade());


        return  course;
    }
    public Course fromDto(int id, CourseDto dto){
        Course course = courseDao.get(id);
        course.setTitle(dto.getTitle());
        course.setStartingDate(LocalDate.parse(dto.getStartingDate()));
        course.setPassingGrade(dto.getPassingGrade());
        course.setTopic(topicDao.getById(dto.getTopicId()));
        course.setPublished(dto.isPublished());
        course.setPassingGrade(dto.getPassingGrade());


        return  course;
    }
}

