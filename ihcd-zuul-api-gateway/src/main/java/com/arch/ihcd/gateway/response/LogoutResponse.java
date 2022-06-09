package com.arch.ihcd.gateway.response;

public class LogoutResponse {
    private String message;
    private String status;

    public LogoutResponse(String message, String status){
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
