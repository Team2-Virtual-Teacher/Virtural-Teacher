package com.alpha53.virtualteacher.exceptions;

public class EntityDuplicateException extends RuntimeException {
    public EntityDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }
    public EntityDuplicateException(String type, String value) {
        super(String.format("%s %s already exists.", type, value));
    }
    public EntityDuplicateException(int userId,int lectureId) {
        super(String.format("Solution for user ID: %d in Lecture with ID: %d already rated", userId, lectureId));
    }
}
