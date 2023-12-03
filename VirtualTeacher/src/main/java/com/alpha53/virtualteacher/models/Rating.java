package com.alpha53.virtualteacher.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Rating {
    private int ratingId;
    private String comment;
    private double rating;
    private User rater;
    private Course course;

}

