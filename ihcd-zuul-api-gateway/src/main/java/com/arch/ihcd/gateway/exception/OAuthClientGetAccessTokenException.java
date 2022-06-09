package com.arch.ihcd.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OAuthClientGetAccessTokenException extends RuntimeException {
    private String details;
    public OAuthClientGetAccessTokenException(String message, String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
