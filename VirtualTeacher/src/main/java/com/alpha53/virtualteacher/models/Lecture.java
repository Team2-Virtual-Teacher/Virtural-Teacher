package com.alpha53.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Lecture {

    private Integer id;

    private String title;

    private String videoUrl;

    private String assignmentUrl;

    private int courseId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LectureDescription description;

}
