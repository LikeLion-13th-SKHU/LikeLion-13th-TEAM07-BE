package com.example.ie_um.member.exception;

import com.example.ie_um.global.error.exception.NotFoundGroupException;

public class MemberNotFoundException extends NotFoundGroupException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
