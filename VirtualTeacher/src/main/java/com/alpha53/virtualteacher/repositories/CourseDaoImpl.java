package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.utilities.mappers.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, @Lazy CourseMapper courseMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

       this.courseMapper = courseMapper;
    }

    @Override
    public Course get(int id) {

        String sql = "SELECT courses.id,title,start_date,creator_id,email,first_name,last_name,profile_picture,is_published,passing_grade,topic,topic_id " +
                     "FROM courses LEFT JOIN topics ON courses.topic_id = topics.id     " +
                "  LEFT JOIN users ON courses.creator_id = users.id WHERE courses.id=:id      ";


        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, in, courseMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityNotFoundException();
        }

    }

    @Override
    public Course getByTitle(String title) {
        String sql = "SELECT courses.id,title,start_date,creator_id,email,first_name,last_name,profile_picture,is_published,passing_grade,topic,topic_id " +
                "FROM courses LEFT JOIN topics ON courses.topic_id = topics.id     " +
                "  LEFT JOIN users ON courses.creator_id = users.id WHERE courses.title=:title      ";


        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("title", title);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, in, courseMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityNotFoundException();
        }
    }

 /*   @Override
    public Course get(String email) {
        return null;
    }*/

    @Override
    public List<Course> getAll() {
        String sql = "SELECT courses.id,title,start_date,creator_id,email,first_name,last_name,profile_picture,is_published,passing_grade,topic,topic_id " +
                "FROM courses LEFT JOIN topics ON courses.topic_id = topics.id     " +
                "  LEFT JOIN users ON courses.creator_id = users.id     ";

        return namedParameterJdbcTemplate.query(sql, courseMapper);

    }
    @Override
    public List<Course> getCoursesByUser( int userId){
        String sql = "SELECT courses.id,title,start_date,creator_id,is_published,passing_grade,topic,topic_id " +
                "FROM courses LEFT JOIN topics ON courses.topic_id = topics.id";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", userId);

        return namedParameterJdbcTemplate.query(sql, in, courseMapper);

    }

    @Override
    public void create(Course course) {

        String sql = "INSERT INTO courses (title, topic_id, start_date,creator_id,is_published,passing_grade)" +
                     "VALUES (:title,:topic_id,:start_date,:creator_id,:is_published,:passing_grade)         ";
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("title", course.getTitle());
        in.addValue("topic_id", course.getTopic().getTopicId());
        in.addValue("start_date", course.getStartingDate());
        in.addValue("creator_id", course.getCreator().getUserId());
        in.addValue("is_published", course.isPublished());
        in.addValue("passing_grade", course.getPassingGrade());

        namedParameterJdbcTemplate.update(sql, in);
    }


    @Override
    public void update(Course course) {

        String sql = "UPDATE courses SET title= :title, topic_id= :topic_id, start_date= :start_date,creator_id= :creator_id, is_published= :is_published, passing_grade= :is_published where courses.id= :id";

         MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("title", course.getTitle());
        in.addValue("topic_id", course.getTopic().getTopicId());
        in.addValue("start_date", course.getStartingDate());
        in.addValue("creator_id", course.getCreator().getUserId());
        in.addValue("is_published", course.isPublished());
        in.addValue("passing_grade", course.getPassingGrade());
        in.addValue("id", course.getCourseId());
        namedParameterJdbcTemplate.update(sql, in);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM courses where id= :id";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", id);
        namedParameterJdbcTemplate.update(sql, in);
    }
}
