package com.alpha53.virtualteacher.models;

public class RatingDto {
    private String comment;
    private double rating;
    private int courseId;

    public RatingDto(String comment, double rating, int courseId) {
        this.comment = comment;
        this.rating = rating;
        this.courseId = courseId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
