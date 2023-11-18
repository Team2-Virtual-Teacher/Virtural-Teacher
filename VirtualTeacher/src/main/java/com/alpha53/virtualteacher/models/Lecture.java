package com.alpha53.virtualteacher.models;

import jakarta.persistence.*;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "video_url")
    private String video_url;

    @Column(name = "assignment_task")
    private String assignment;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Lecture(int id, String title, String video_url, String assignment, Course course) {
        this.id = id;
        this.title = title;
        this.video_url = video_url;
        this.assignment = assignment;
        this.course = course;
    }

    public Lecture() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
