package com.alpha53.virtualteacher.models;

import java.util.Optional;

public class FilterOptions {

    private Optional<String> title;
    private Optional<String> topic;
    private Optional<String> teacher;
    private Optional<String> rating;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions(String title, String topic,String teacher, String rating, String sortBy, String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.topic = Optional.ofNullable(topic);
        this.teacher = Optional.ofNullable(teacher);
        this.rating = Optional.ofNullable(rating);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getTopic() {
        return topic;
    }

    public void setTopic(Optional<String> topic) {
        this.topic = topic;
    }

    public Optional<String> getTeacher() {
        return teacher;
    }

    public void setTeacher(Optional<String> teacher) {
        this.teacher = teacher;
    }

    public Optional<String> getRating() {
        return rating;
    }

    public void setRating(Optional<String> rating) {
        this.rating = rating;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }
}
