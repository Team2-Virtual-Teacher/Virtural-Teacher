package com.alpha53.virtualteacher.exceptions;
public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }
}