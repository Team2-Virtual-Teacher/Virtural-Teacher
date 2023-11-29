package com.alpha53.virtualteacher.models.dtos;

import com.alpha53.virtualteacher.models.LectureDescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @Size(min = 5, max = 50)
    private String title;

    @NotNull
    @NotBlank
    @Length(min = 10,max = 2048)
    private String videoUrl;

    private LectureDescription description;
}
