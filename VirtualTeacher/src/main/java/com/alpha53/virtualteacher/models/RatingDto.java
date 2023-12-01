package com.alpha53.virtualteacher.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RatingDto {
    private String comment;
    private double rating;
    private int courseId;

}
