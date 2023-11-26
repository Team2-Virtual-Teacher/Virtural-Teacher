package com.alpha53.virtualteacher.models.dtos;

import com.alpha53.virtualteacher.models.LectureDescription;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LectureDto {

    private Integer id;

    @NotNull
    @Size(min = 5, max = 50)
    private String title;

    @NotNull
    @NotBlank
    @Length(min = 10,max = 2048)
    private String videoUrl;

    @NotNull
    @NotBlank
    private String assignment;


   /* @Min(value = 1, message = "Course Id must be an positive Integer")
    @Max(value = Integer.MAX_VALUE, message = "Course Id must be less than" + Integer.MAX_VALUE)
    private int courseId;*/

    private LectureDescription description;
}
