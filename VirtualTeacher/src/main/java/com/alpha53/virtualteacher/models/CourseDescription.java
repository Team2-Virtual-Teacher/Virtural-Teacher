package com.alpha53.virtualteacher.models;

public class CourseDescription {
    private Course course;
    private String description;

    public CourseDescription(Course course, String description) {
        this.course = course;
        this.description = description;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
