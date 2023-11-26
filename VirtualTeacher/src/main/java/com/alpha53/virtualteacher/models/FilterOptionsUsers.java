package com.alpha53.virtualteacher.models;

import java.util.Optional;

public class FilterOptionsUsers {

    private Optional<String> email;
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> roleType;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptionsUsers() {
    }

    public FilterOptionsUsers( String email, String firstName, String lastName,String roleType, String sortBy, String sortOrder) {
        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.roleType = Optional.ofNullable(roleType);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public void setLastName(Optional<String> lastName) {
        this.lastName = lastName;
    }

    public Optional<String> getRoleType() {
        return roleType;
    }

    public void setRoleType(Optional<String> roleType) {
        this.roleType = roleType;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }
}
