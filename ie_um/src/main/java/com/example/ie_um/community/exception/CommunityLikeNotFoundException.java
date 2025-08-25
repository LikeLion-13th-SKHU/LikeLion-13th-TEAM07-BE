package com.example.ie_um.community.exception;

import com.example.ie_um.global.error.exception.NotFoundGroupException;

public class CommunityLikeNotFoundException extends NotFoundGroupException {
    public  CommunityLikeNotFoundException(String message) {
        super(message);
    }
}
