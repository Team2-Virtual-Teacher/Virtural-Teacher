package com.alpha53.virtualteacher.models.dtos;

import com.alpha53.virtualteacher.models.CourseDescription;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourseDto {

    private String title;

    private int topicId;

    private String startingDate;

    private boolean isPublished;

    private double passingGrade;
    private CourseDescription description;


}
