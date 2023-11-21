package com.alpha53.virtualteacher.models;

public class Grade {
    private int gradeId;
    private double grade;
    private int assignment_id;

    public Grade(int gradeId, double grade, int assignment_id) {
        this.gradeId = gradeId;
        this.grade = grade;
        this.assignment_id = assignment_id;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }
}
