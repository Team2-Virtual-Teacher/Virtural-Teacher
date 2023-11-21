package com.alpha53.virtualteacher.models.dtos;

import com.alpha53.virtualteacher.models.Topic;

import java.time.LocalDate;

public class CourseDto {

    private String title;

    private Topic topic;

    private int creator;

    private LocalDate startingDate;

    private boolean isPublished;

    private double passingGrade;

    public CourseDto(String title, Topic topic, int creator, LocalDate startingDate, boolean isPublished, double passingGrade) {
        this.title = title;
        this.topic = topic;
        this.creator = creator;
        this.startingDate = startingDate;
        this.isPublished = isPublished;
        this.passingGrade = passingGrade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public double getPassingGrade() {
        return passingGrade;
    }

    public void setPassingGrade(double passingGrade) {
        this.passingGrade = passingGrade;
    }
}
