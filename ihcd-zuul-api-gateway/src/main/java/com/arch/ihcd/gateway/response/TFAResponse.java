package com.arch.ihcd.gateway.response;

public class TFAResponse {
    private String verifyType; //Mobile/Email
    private String message;

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
