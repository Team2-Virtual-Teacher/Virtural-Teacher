package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Solution;
import com.alpha53.virtualteacher.repositories.contracts.SolutionDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SolutionDaoImpl extends NamedParameterJdbcDaoSupport implements SolutionDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SolutionDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.setDataSource(dataSource);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Solution> getAllByLectureId(int lectureId) {
        String sql = "SELECT id as solutionId,solution_url as solutionUrl,user_id as userId, lecture_id as lectureId " +
                "FROM solutions WHERE lecture_id=:lectureId";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("lectureId", lectureId);

        return namedParameterJdbcTemplate.query(sql, param, new BeanPropertyRowMapper<>(Solution.class));
    }

    @Override
    public List<Solution> getAllByUserId(int userId) {
        String sql = "SELECT id as solutionId,solution_url as solutionUrl,user_id as userId, lecture_id as lectureId " +
                "FROM solutions WHERE user_id=:userId";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        return namedParameterJdbcTemplate.query(sql, param, new BeanPropertyRowMapper<>(Solution.class));
    }

    @Override
    public Optional<String> getSolutionUrl(int lectureId) {
        String sql = "SELECT solution_url FROM solutions WHERE lecture_id=:lectureId";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("lectureId", lectureId);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, param, String.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addSolution(int userId, int lectureId, String fileUrl) {
        String sql = "INSERT INTO solutions (solution_url,user_id,lecture_id) " +
                "VALUES (:fileUrl,:userId,:lectureId)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("fileUrl", fileUrl);
        params.addValue("userId", userId);
        params.addValue("lectureId", lectureId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void updateSolutionUrl(int userId, int lectureId, String fileUrl) {
        String sql = "UPDATE solutions SET solution_url =:fileUrl WHERE lecture_id = :lectureId";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("fileUrl", fileUrl);
        param.addValue("lectureId", lectureId);

        namedParameterJdbcTemplate.update(sql, param);
    }

    @Override
    public Solution getSolution(int userId, int lectureId) {
        String sql = "SELECT solution_url, user_id, lecture_id, id, grade FROM solutions WHERE lecture_id =:lectureId AND user_id=:lectureId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("lectureId", lectureId);
        params.addValue("lectureId", lectureId);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Solution.class));
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityNotFoundException(userId, lectureId);
        }
    }

    @Override
    public void addGrade(Solution solution) {
        String sql = "UPDATE solutions SET grade=:grade WHERE user_id=:userId AND lecture_id=:lectureId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("grade", solution.getGrade());
        params.addValue("userId", solution.getUserId());
        params.addValue("lectureId", solution.getLectureId());
        namedParameterJdbcTemplate.update(sql, params);
    }
    @Override
    public Map<Integer, Double> getSolutionCountAndAVGPerStudent(int studentId, int courseId) {
        String sql = "SELECT COUNT(user_id) as solution_count, AVG(grade) as avg_grade " +
                " FROM solutions                                                       " +
                " LEFT JOIN lectures ON solutions.lecture_id = lectures.id             " +
                " WHERE user_id = :studentId                                           " +
                "   AND grade >= 2                                                     " +
                "   AND lectures.course_id = :courseId                                 " +
                " GROUP BY lectures.course_id                                          ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("studentId", studentId);
        params.addValue("courseId", courseId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                Map<Integer, Double> result = new HashMap<>();
                result.put(rs.getInt("solution_count"),
                        (rs.getDouble("avg_grade")));
                return result;
            });
        } catch (EmptyResultDataAccessException e) {
           Map<Integer,Double> r = new HashMap<>();
           r.put(0,0.0);
           return r;
        }
    }
}
