package com.alpha53.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Lecture {

    private Integer id;

    private String title;

    private String videoUrl;

    private String assignment;

    private int courseId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LectureDescription description;

}
