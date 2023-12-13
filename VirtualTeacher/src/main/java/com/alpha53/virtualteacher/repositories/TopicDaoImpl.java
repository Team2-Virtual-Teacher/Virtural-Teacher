package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.repositories.contracts.TopicDao;
import com.alpha53.virtualteacher.utilities.mappers.TopicMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TopicDaoImpl extends NamedParameterJdbcDaoSupport implements TopicDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TopicMapper topicMapper = new TopicMapper();
    public TopicDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.setDataSource(dataSource);
    }
    @Override
    public List<Topic> getAll() {
        String sql = "SELECT * FROM topics";
        return namedParameterJdbcTemplate.query(sql,topicMapper);
    }

    @Override
    public Topic getById(int id) {
        String sql = "SELECT * FROM topics WHERE id=:id";
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql,in,topicMapper);
    }
}
