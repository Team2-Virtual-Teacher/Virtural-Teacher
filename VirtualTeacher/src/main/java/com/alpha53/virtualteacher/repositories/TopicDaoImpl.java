package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.repositories.contracts.TopicDao;
import com.alpha53.virtualteacher.utilities.mappers.TopicMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TopicDaoImpl extends NamedParameterJdbcDaoSupport implements TopicDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TopicDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.setDataSource(dataSource);
    }



   /* @Autowired
    public TopicDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }*/

    @Override
    public List<Topic> getAll() {
        String sql = "SELECT * FROM topics";
        return namedParameterJdbcTemplate.query(sql,new TopicMapper());
    }

    @Override
    public Topic getById(int id) {
        return new Topic(3, "Test3");
    }
}
