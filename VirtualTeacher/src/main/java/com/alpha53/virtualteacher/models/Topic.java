package com.alpha53.virtualteacher.models;


public class Topic {
    private int topicId;
    private String topic;

    public Topic(int id, String topic) {
        this.topicId = id;
        this.topic = topic;
    }

    public Topic() {
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
