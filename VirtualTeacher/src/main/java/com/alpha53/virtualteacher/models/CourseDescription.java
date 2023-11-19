package com.alpha53.virtualteacher.models;

import jakarta.persistence.*;

@Entity
@Table(name = "course_description")
public class CourseDescription {

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "description")
    private String description;

    //TODO remove id if possible
    @Id
    private int id;

    public CourseDescription(Course course, String description, int id) {
        this.course = course;
        this.description = description;
        this.id = id;
    }

    public CourseDescription() {
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
