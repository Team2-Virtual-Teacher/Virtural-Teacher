package com.alpha53.virtualteacher.models;

public class Rating {
    private int ratingId;
    private String comment;
    private double rating;
    private User rater;
    private Course course;

    public Rating(int ratingId, String comment, double rating, User rater, Course course) {
        this.ratingId = ratingId;
        this.comment = comment;
        this.rating = rating;
        this.rater = rater;
        this.course = course;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
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

    public User getRater() {
        return rater;
    }

    public void setRater(User rater) {
        this.rater = rater;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

