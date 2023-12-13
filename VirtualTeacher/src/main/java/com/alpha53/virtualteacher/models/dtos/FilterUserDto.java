package com.alpha53.virtualteacher.models.dtos;

import lombok.Data;

@Data
public class FilterUserDto {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String sortBy;
    private String sortOrder;
}
