package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.utilities.mappers.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    public CourseDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

    }

    @Override
    public Course get(int id) {

        String sql = "SELECT courses.id,title,start_date,creator_id,is_published,passing_grade,topic,topic_id " +
                     "FROM courses LEFT JOIN topics ON courses.topic_id = topics.id WHERE courses.id=:id      ";


        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, in, new CourseMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException();
        }

    }

 /*   @Override
    public Course get(String email) {
        return null;
    }*/

    @Override
    public List<Course> getAll() {
        String sql = "SELECT courses.id,title,start_date,creator_id,is_published,passing_grade,topic,topic_id " +
                     "FROM courses LEFT JOIN topics ON courses.topic_id = topics.id                           ";

        return namedParameterJdbcTemplate.query(sql, new CourseMapper());

    }
    @Override
    public List<Course> getCoursesByUser( int userId){
        String sql = "SELECT courses.id,title,start_date,creator_id,is_published,passing_grade,topic,topic_id " +
                "FROM courses LEFT JOIN topics ON courses.topic_id = topics.id";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", userId);

        return namedParameterJdbcTemplate.query(sql, in, new CourseMapper());

    }

    @Override
    public void create(Course course) {

        String sql = "INSERT INTO courses (title, topic_id, start_date,creator_id,is_published,passing_grade)" +
                     "VALUES (:title,:topic_id,:start_date,:creator_id,:is_published,:passing_grade)         ";
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("title", course.getTitle());
        in.addValue("topic_id", course.getTopic());
        in.addValue("start_date", course.getStartingDate());
        in.addValue("creator_id", course.getCreator());
        in.addValue("is_published", course.isPublished());
        in.addValue("passing_grade", course.getPassingGrade());

        namedParameterJdbcTemplate.update(sql, in);
    }


    @Override
    public void update(Course user) {

    }

    @Override
    public void delete(int id) {

    }
}
