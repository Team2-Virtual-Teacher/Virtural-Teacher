package com.alpha53.virtualteacher.models.dtos;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailForm {
    @Email(message = "Invalid email address.")
    private String email;

}
