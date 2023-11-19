package com.alpha53.virtualteacher.models;

public enum UserRoles {
    STUDENT(1),
    TEACHER(2),
    ADMIN(3);
    final int value;

    UserRoles(int value){
        this.value=value;
    }
    @Override
    public String toString() {
        return switch (this) {
            case STUDENT -> "Student";
            case TEACHER -> "Teacher";
            case ADMIN -> "Admin";
            default -> throw new IllegalArgumentException();
        };
    }
    public int getValue(){
        return value;
    }
}
