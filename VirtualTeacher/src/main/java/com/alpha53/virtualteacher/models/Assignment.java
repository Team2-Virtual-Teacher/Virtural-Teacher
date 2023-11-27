package com.alpha53.virtualteacher.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Assignment {
    private int assignmentId;
    private String assignmentUrl;
    private int userId;
    private int lectureId;

}
