package com.alpha53.virtualteacher.models;

public class Lecture {
    private int id;
    private String title;
    private String videoUrl;
    private String assignment;
    private int courseId;
    private LectureDescription description;

    public Lecture(int id, String title, String video_url, String assignment, int courseId, LectureDescription description) {
        this.id = id;
        this.title = title;
        this.videoUrl = video_url;
        this.assignment = assignment;
        this.courseId = courseId;
        this.description = description;
    }

    public Lecture() {

    }

    public LectureDescription getDescription() {
        return description;
    }

    public void setDescription(LectureDescription description) {
        this.description = description;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public int getCourse() {
        return courseId;
    }

    public void setCourse(int courseId) {
        this.courseId = courseId;
    }
}
