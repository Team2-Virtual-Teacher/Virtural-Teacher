package com.alpha53.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

public class User {
    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private String pictureUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Course> courses;

    public User(int userId, String email, String password, String firstName, String lastName, Role role, String pictureUrl, Set<Course> courses) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.pictureUrl = pictureUrl;
        this.courses = courses;
    }

    public User() {

    }

    public User(int userId, String email, String password, String firstName, String lastName, Role role, String pictureUrl) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.pictureUrl = pictureUrl;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureAddress) {
        this.pictureUrl = pictureAddress;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
