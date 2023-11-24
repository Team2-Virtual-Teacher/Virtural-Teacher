package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Topic;

import java.util.List;

public interface TopicDao {
    List<Topic> getAll();
    Topic getById( int id );

}
