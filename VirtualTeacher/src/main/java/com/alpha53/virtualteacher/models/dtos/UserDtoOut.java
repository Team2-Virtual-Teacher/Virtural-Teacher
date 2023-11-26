package com.alpha53.virtualteacher.models.dtos;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserDtoOut {

    @NotNull
    private int userId;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 2,max = 20,message = "First name must be between 2 and 20 symbols")
    private String firstName;

    @NotNull
    @Size(min = 2,max = 20,message = "Last name must be between 2 and 20 symbols")
    private String lastName;

    @NotNull
    private Role role;

    @NotNull
    private String pictureUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Course> courses;

    public UserDtoOut() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPictureUrl(){
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }


    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
