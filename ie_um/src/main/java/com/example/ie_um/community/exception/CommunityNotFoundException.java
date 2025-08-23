package com.example.ie_um.community.exception;

import com.example.ie_um.global.error.exception.NotFoundGroupException;

public class CommunityNotFoundException extends NotFoundGroupException {
    public  CommunityNotFoundException(String message) {
        super(message);
    }
}
