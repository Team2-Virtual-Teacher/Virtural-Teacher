package com.alpha53.virtualteacher.models;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Solution {
    @Positive(message = "User ID must be positive integer")
    private int userId;
    @Positive(message = "User ID must be positive integer")
    private int lectureId;
    private int solutionId;
    private String solutionUrl;
    private double grade;

    public Solution(int userId, int lectureId, double grade) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.grade = grade;
    }
}
