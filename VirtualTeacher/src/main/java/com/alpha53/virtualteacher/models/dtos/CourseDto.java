package com.alpha53.virtualteacher.models.dtos;

import com.alpha53.virtualteacher.models.Topic;

import java.sql.Date;
import java.time.LocalDate;

public class CourseDto {

    private String title;

    private int topicId;


    private String startingDate;

    private boolean isPublished;

    private double passingGrade;
    private String description;

    public CourseDto(String title, int topicId, String startingDate, boolean isPublished, double passingGrade, String description) {
        this.title = title;
        this.topicId=topicId;
        this.startingDate = startingDate;
        this.isPublished = isPublished;
        this.passingGrade = passingGrade;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }


    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
