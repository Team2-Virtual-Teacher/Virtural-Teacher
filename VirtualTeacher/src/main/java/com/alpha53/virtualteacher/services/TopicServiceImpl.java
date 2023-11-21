package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.models.Topic;
import com.alpha53.virtualteacher.repositories.contracts.TopicDao;
import com.alpha53.virtualteacher.services.contracts.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicDao topicDao;

    public TopicServiceImpl(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    public List<Topic> getAll(){
        return topicDao.getAll();
    }
}
