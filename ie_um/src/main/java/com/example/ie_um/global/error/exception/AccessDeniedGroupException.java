package com.example.ie_um.global.error.exception;

public abstract class AccessDeniedGroupException extends RuntimeException {
    public AccessDeniedGroupException(String message) {
        super(message);
    }
}