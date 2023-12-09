package com.alpha53.virtualteacher.models.dtos;

import lombok.Data;

@Data
public class FilterOptionDto {
    private String title;
    private String topic;
    private String teacher;
    private Double rating;
    private Boolean  isPublic;
    private String sortBy;
    private String sortOrder;
}
