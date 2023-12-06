package com.alpha53.virtualteacher.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LoginDto {

    @NotEmpty(message = "Email can't be empty")
    private String email;

    @NotEmpty(message = "Password can't be empty")
    private String password;



}

