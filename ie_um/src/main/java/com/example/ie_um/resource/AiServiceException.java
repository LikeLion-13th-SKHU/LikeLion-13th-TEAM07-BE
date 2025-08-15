package com.example.ie_um.resource;

import com.example.ie_um.global.error.exception.BusinessException;

public class AiServiceException extends BusinessException {
    public AiServiceException(String message) {
        super(message);
    }
}
