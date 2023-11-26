package com.alpha53.virtualteacher.models;


import java.time.LocalDate;
import java.util.Objects;


public class Course {
    private Integer courseId;
    private String title;
    private Topic topic;
    private User creator;
    private LocalDate startingDate;
    private boolean isPublished;
    private double passingGrade;
    private CourseDescription description;

    public Course(LocalDate startingDate) {
        this.startingDate = startingDate;
        this.isPublished = false;
    }

    public Course() {
    }

    public Course(Integer id, String title, Topic topic, User creator, LocalDate startingDate, boolean isPublished, double passingGrade, CourseDescription description) {
        this.courseId = id;
        this.title = title;
        this.topic = topic;
        this.creator = creator;
        this.startingDate = startingDate;
        this.isPublished = isPublished;
        this.passingGrade = passingGrade;
        this.description = description;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
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

    public CourseDescription getDescription() {
        return description;
    }

    public void setDescription(CourseDescription description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId == course.courseId && Objects.equals(title, course.title) && Objects.equals(topic, course.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, title, topic);
    }
}
