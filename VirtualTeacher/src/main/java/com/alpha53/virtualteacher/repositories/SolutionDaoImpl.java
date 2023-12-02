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
import java.util.List;
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

}
