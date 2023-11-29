package com.alpha53.virtualteacher.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Solution {
    private int solutionId;
    private String solutionUrl;
    private int userId;
    private int lectureId;

}
