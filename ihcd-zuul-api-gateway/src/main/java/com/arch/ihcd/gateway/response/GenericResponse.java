package com.arch.ihcd.gateway.response;

public class GenericResponse {

    private String message;
    public GenericResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
