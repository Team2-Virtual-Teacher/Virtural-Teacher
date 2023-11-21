package com.alpha53.virtualteacher.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotNull
    @Email
    private String email;

    //@JsonIgnore
    @NotNull
    @Size(min = 8,max = 32,message = "Password must be between 8 and 32 symbols")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",message = "Password does not match requirements (ex. P@ss1234).")
    private String password;

    @NotNull
    @Size(min = 2,max = 20,message = "First name must be between 2 and 20 symbols")
    private String firstName;

    @NotNull
    @Size(min = 2,max = 20,message = "Last name must be between 2 and 20 symbols")
    private String lastName;

    @NotNull
    private String role;

    @NotNull
    private String pictureUrl;

    public UserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPictureUrl(){
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
