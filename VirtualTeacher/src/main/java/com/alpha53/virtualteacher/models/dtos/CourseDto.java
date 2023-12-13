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
    @NonNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String title;
    @NonNull

    private int topicId;
    @NonNull
    @NotBlank
    private String startingDate;

    private boolean isPublished;

    private double passingGrade;
    private CourseDescription description;


}
