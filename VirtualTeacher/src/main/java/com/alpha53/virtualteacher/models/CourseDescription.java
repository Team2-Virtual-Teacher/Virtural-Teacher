package com.alpha53.virtualteacher.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourseDescription {
    private int courseId;
    private String description;
// TODO: 4.12.23 We can eventually combine this model with LectureDescription and use one and the same.

}
