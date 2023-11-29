package com.alpha53.virtualteacher.models;


import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Course {
    private Integer courseId;
    private String title;
    private Topic topic;
    private User creator;
    private LocalDate startingDate;
    private boolean isPublished;
    private double passingGrade;
    private double avgRating;
    private CourseDescription description;
    private Set<Lecture> lectures = new HashSet<>();


}
