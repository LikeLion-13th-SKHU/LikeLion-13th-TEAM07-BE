package com.example.ie_um.global.annotation.exception;


import com.example.ie_um.global.error.exception.AuthGroupException;

public class UnauthorizedException extends AuthGroupException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
