package com.example.ie_um.accompany.exception;

import com.example.ie_um.global.error.exception.NotFoundGroupException;

public class AccompanyNotFoundException extends NotFoundGroupException {
    public AccompanyNotFoundException(String message) {
        super(message);
    }
}
