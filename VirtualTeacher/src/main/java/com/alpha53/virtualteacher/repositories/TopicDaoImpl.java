package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.repositories.contracts.TopicDao;
import com.alpha53.virtualteacher.utilities.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TopicDaoImpl implements TopicDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TopicDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Topic> getAll() {
        String sql = "SELECT * FROM topics";
        return namedParameterJdbcTemplate.query(sql,new TopicMapper());
    }
}
