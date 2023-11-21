package com.alpha53.virtualteacher.utilities;

import com.alpha53.virtualteacher.models.Topic;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TopicMapper implements RowMapper<Topic> {

    @Override
    public Topic mapRow(ResultSet rs, int rowNum) throws SQLException {
        Topic topic = new Topic();
        topic.setTopicId(rs.getInt("id"));
        topic.setTopic(rs.getString("topic"));
        return topic;
    }
}
