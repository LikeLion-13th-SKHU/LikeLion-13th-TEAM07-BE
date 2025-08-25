package com.example.ie_um.global.gcs.exception;


import com.example.ie_um.global.error.exception.NotFoundGroupException;

public class GCSFileNotFoundException extends NotFoundGroupException {
    public GCSFileNotFoundException(String message) {
        super(message);
    }
}
