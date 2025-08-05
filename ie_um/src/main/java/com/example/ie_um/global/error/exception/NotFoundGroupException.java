package com.example.ie_um.global.error.exception;

public abstract class NotFoundGroupException extends RuntimeException{
    public NotFoundGroupException(String message) {
        super(message);
    }
}
