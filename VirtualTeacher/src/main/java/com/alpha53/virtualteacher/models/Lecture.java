package com.alpha53.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Lecture {

    private Integer id;

    private String title;

    private String videoUrl;

    private String assignmentUrl;

    private int courseId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LectureDescription description;

}
