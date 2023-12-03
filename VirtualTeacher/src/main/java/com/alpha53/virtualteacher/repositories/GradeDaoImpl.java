/*
package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Grade;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.GradeDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GradeDaoImpl implements GradeDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GradeDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Grade> getAll(User user, Course course) {
        String sql = "SELECT * FROM grades WHERE user_id =:userId AND course_id =:courseId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", user.getUserId());
        params.addValue("courseId", course.getCourseId());

        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Grade.class));

    }

    @Override
    public double getAvgGrade(User user, Course course) {
        String sql = "SELECT AVG(grade) FROM grades WHERE (user_id=:userId AND course_id=:courseId) GROUP BY user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId",user.getUserId());
        params.addValue("courseId",course.getCourseId());

        try {
            return namedParameterJdbcTemplate.queryForObject(sql,params,Double.class);
        }catch (EmptyResultDataAccessException e){
           return 0;
        }
    }

    @Override
    public void update(Grade grade) {
        String sql = "UPDATE grades SET grade = :grade WHERE (user_id=:userId AND lecture_id=:lectureId AND course_id =:courseId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("grade", grade.getGrade());
        params.addValue("userId", grade.getUserId());
        params.addValue("lectureId", grade.getLectureId());
        params.addValue("courseId", grade.getCourseId());

        namedParameterJdbcTemplate.update(sql, params);

    }

    @Override
    public void create(Grade grade) {
        String sql = "INSERT INTO grades(user_id,lecture_id,course_id,grade) VALUES(:userId,:lectureId,:courseId,:grade)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId",grade.getUserId());
        params.addValue("lectureId",grade.getLectureId());
        params.addValue("courseId",grade.getCourseId());
        params.addValue("grade",grade.getGrade());

        namedParameterJdbcTemplate.update(sql,params);
    }
}
*/
