package com.example.ie_um.member.exception;

import com.example.ie_um.global.error.exception.BusinessException;

public class DuplicateNickNameException extends BusinessException {

    public DuplicateNickNameException(String message) {
        super(message);
    }
}
