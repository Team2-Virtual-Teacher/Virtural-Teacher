package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.utilities.LectureMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class LectureDaoImpl extends NamedParameterJdbcDaoSupport implements LectureDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public LectureDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.setDataSource(dataSource);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Get lecture by ID
     *
     * @param id - lecture ID
     * @return Lecture object
     * @Throws EntityNotFoundException if lecture does not exist
     */
    @Override
    public Lecture get(final int id) {

        String sql = "SELECT * FROM lectures LEFT JOIN lecture_description ON lectures.id = lecture_description.lecture_id WHERE id = :id";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, param, new LectureMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityNotFoundException("Lecture", "id", String.valueOf(id));
        }
    }

    /**
     * Get all lectures by course
     *
     * @param courseId - course ID
     * @return list of Lectures
     */
    @Override
    public List<Lecture> getAllByCourseId(int courseId) {
        String sql =
                "SELECT * FROM lectures                          " +
                        "LEFT JOIN lecture_description                   " +
                        "ON lectures.id = lecture_description.lecture_id " +
                        "WHERE course_id = :id                           ";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", courseId);
        return namedParameterJdbcTemplate.query(sql, param, new LectureMapper());
    }

    /**
     * Create lecture
     *
     * @param lecture - Lecture to be created
     */
    @Override
    public int create(Lecture lecture) {
        String sql =
                "INSERT INTO lectures(title,video_url,assignment_url,course_id) " +
                        "     VALUES (:title,:videoUrl,:assignmentUrl,:courseId)    ";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("title", lecture.getTitle());
        param.addValue("videoUrl", lecture.getVideoUrl());
        param.addValue("assignmentUrl", lecture.getAssignmentUrl());
        param.addValue("courseId", lecture.getCourseId());
        int executeResult = namedParameterJdbcTemplate.update(sql, param);
        if (lecture.getDescription() != null) {
            addDescription(lecture, param);
        }
        return executeResult;
    }

    /**
     * Update lecture
     * -If the existing lecture does not have description,
     * but description is present into parameter lecture, new description is added.
     * -If the existing lecture have description, but no description present into parameter lecture,
     * description of the existing lecture is deleted.
     * -If existing  and parameter lecture has description, description of the parameter lecture replace the existing
     *
     * @param lecture - details of the lecture to be inserted into existing lecture
     * @Throws EntityNotFoundException if no rows was affected
     */

    @Override
    public void update(Lecture lecture) {
        String sql = "UPDATE lectures SET title = :title, video_url = :videoUrl,assignment_url = :assignmentUrl,course_id =:courseId WHERE id= :lectureId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("lectureId", lecture.getId());
        params.addValue("title", lecture.getTitle());
        params.addValue("videoUrl", lecture.getVideoUrl());
        params.addValue("assignmentUrl", lecture.getAssignmentUrl());
        params.addValue("courseId", lecture.getCourseId());

        if (namedParameterJdbcTemplate.update(sql, params) == 0) {
            throw new EntityNotFoundException("Lecture", "id", lecture.getId().toString());
        }
        if (isDescriptionExist(lecture.getId())) {
            if (lecture.getDescription() == null) {
                deleteDescription(params);
            } else {
                params.addValue("description", lecture.getDescription().getDescription());
                updateDescription(params);
            }
        } else if (lecture.getDescription() != null) {
            addDescription(lecture, params);
        }
    }

    /**
     * Delete lecture
     *
     * @param lectureId = ID of the lecture to be deleted
     * @return number of the affected rows, 0 if nothing deleted
     */
    @Override
    public int delete(int lectureId) {
        String sql = "DELETE FROM lectures WHERE id =:lectureId";
        MapSqlParameterSource params = new MapSqlParameterSource("lectureId", lectureId);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * Return creator ID of the course by given lecture ID
     *
     * @param lectureId - ID of the lecture
     * @return ID of the creator of the given lecture
     */

    @Override
    public Optional<String> getAssignmentUrl(int lectureId){
        String sql = "SELECT assignment_url FROM lectures WHERE id=:lectureId";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("lectureId",lectureId);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, param, String.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isAssignmentExist(int lectureId) {
        String sql = "SELECT COUNT(*) FROM lectures WHERE id =:lectureId";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("lectureId", lectureId);
        Integer result = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class);
        return result!=null && result>0;
    }

    /**
     * Check if lecture has description
     *
     * @param lectureId - ID of a lecture
     * @return true if description exist, otherwise false
     */
    private boolean isDescriptionExist(int lectureId) {
        String sql =
                "SELECT COUNT(*) FROM lecture_description WHERE lecture_id = :lectureId";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("lectureId", lectureId);
        Integer result = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class);
        return result!=null && result>0;
    }

    /**
     * Add description of a lecture
     * It's a helper method
     *
     * @param lecture - Lecture containing description and lecture ID
     */
    private void addDescription(Lecture lecture, MapSqlParameterSource param) {
        if (lecture.getId() == null) {
            String lectureIdSql = "SELECT id FROM lectures WHERE course_id = :courseId AND title =:title";
            lecture.setId(namedParameterJdbcTemplate.queryForObject(lectureIdSql, param, Integer.class));
        }
        String descriptionSql = "INSERT INTO lecture_description (lecture_id,description) VALUES (:lecture_id,:description) ";
        param.addValue("lecture_id", lecture.getId());
        param.addValue("description", lecture.getDescription().getDescription());
        namedParameterJdbcTemplate.update(descriptionSql, param);
    }

    /**
     * Delete description
     * It's a helper method
     */
    private void deleteDescription(MapSqlParameterSource params) {
        String sql = "DELETE FROM lecture_description WHERE lecture_id =:lectureId";
        namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * Update description
     * It's a helper method
     */
    private void updateDescription(MapSqlParameterSource params) {
        String sql = "UPDATE lecture_description SET description =:description WHERE lecture_id =:lectureId";
        namedParameterJdbcTemplate.update(sql, params);
    }
}
