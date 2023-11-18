package com.alpha53.virtualteacher.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating")
    private double rating;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User rater;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Rating() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating1 = (Rating) o;
        return id == rating1.id && Double.compare(rating, rating1.rating) == 0 && Objects.equals(comment, rating1.comment) && Objects.equals(rater, rating1.rater) && Objects.equals(course, rating1.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, rating, rater, course);
    }
}

