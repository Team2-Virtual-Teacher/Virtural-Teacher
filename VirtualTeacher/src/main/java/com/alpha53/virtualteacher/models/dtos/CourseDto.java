package com.alpha53.virtualteacher.models.dtos;

import com.alpha53.virtualteacher.models.CourseDescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourseDto {

    @NotBlank
    @Size(min = 5, max = 50)
    private String title;

    @NotBlank
    private int topicId;

    @NotBlank
    private String startingDate;

    private boolean isPublished;

    private double passingGrade;
    private CourseDescription description;


}
