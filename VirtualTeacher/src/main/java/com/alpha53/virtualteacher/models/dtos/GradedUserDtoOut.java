package com.alpha53.virtualteacher.models.dtos;

import lombok.Data;

@Data
public class GradedUserDtoOut {
    int userId;
    int lectureId;
    int solutionId;
    int courseId;
    double grade;
    String email;
    String firstName;
    String lastName;
    String solutionUrl;

}
