package com.alpha53.virtualteacher.models;

public class Assignment {
    private int assignmentId;
    private String assignment;
    private User user;
    private Lecture lecture;

    public Assignment(int assignmentId, String assignment, User user, Lecture lecture) {
        this.assignmentId = assignmentId;
        this.assignment = assignment;
        this.user = user;
        this.lecture = lecture;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
