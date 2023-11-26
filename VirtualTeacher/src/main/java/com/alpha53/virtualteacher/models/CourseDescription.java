package com.alpha53.virtualteacher.models;

public class CourseDescription {
    private int courseId;
    private String description;

    public CourseDescription(int courseId, String description) {
        this.courseId = courseId;
        this.description = description;
    }

    public CourseDescription() {

    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
