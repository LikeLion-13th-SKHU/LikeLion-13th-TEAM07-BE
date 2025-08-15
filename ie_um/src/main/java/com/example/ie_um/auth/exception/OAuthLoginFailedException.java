package com.example.ie_um.auth.exception;

import com.example.ie_um.global.error.exception.AuthGroupException;

public class OAuthLoginFailedException extends AuthGroupException {
    public OAuthLoginFailedException(String message) {
        super("OAuth 로그인 실패: " + message);
    }
}
