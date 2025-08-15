package com.example.ie_um.auth.exception;

import com.example.ie_um.global.error.exception.AuthGroupException;

public class UnsupportedProviderException extends AuthGroupException {
    public UnsupportedProviderException(String provider) {
        super("지원하지 않는 OAuth Provider입니다.: " + provider);
    }
}
