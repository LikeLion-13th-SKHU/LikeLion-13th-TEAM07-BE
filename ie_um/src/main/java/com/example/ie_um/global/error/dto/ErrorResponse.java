package com.example.ie_um.global.error.dto;

public record ErrorResponse(
    int statusCode,
    String message
) {

}
