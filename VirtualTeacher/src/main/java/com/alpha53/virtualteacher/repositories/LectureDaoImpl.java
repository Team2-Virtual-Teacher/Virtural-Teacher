package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.LectureDescription;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.utilities.LectureDescriptionMapper;
import com.alpha53.virtualteacher.utilities.LectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureDaoImpl implements LectureDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LectureDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Lecture get(int id) {
        return null;
    }

    @Override
    public List<Lecture> getAllByCourseId(int id) {
        String sql = "SELECT * FROM lectures  WHERE course_id = :id";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedParameterJdbcTemplate.query(sql, param,new LectureMapper());
    }

    public LectureDescription getDescriptionByLectureId(int id) {
        String sql = "SELECT * FROM lecture_description WHERE lecture_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, new LectureDescriptionMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public void create(Lecture lecture) {
        String sql = "INSERT INTO lectures(title,video_url,assignment_task,course_id) " +
                     "VALUES (:title,:videoUrl,:assignment,:courseId)                 ";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("title", lecture.getTitle());
        param.addValue("videoUrl", lecture.getVideoUrl());
        param.addValue("assignment", lecture.getAssignment());
        param.addValue("courseId", lecture.getCourse());
        namedParameterJdbcTemplate.update(sql, param);
    }

    @Override
    public void update(Lecture lecture) {


    }

    @Override
    public void delete(int id) {

    }

    /*private LectureDescription mapRow(ResultSet rs, int rowNum){
        LectureDescription lectureDescription = new LectureDescription();

        lectureDescription.setDescriptionId(rs.getInt(""));
        return null;
    }*/
}
