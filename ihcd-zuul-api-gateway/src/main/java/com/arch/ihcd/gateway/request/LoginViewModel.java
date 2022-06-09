package com.arch.ihcd.gateway.request;

public class LoginViewModel {
    private String username;
    private String password;
    private String qrauthcode;
    private String mobileotp;
    private String emailotp;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQrauthcode() {
        return qrauthcode;
    }

    public void setQrauthcode(String qrauthcode) {
        this.qrauthcode = qrauthcode;
    }

    public String getMobileotp() {
        return mobileotp;
    }

    public void setMobileotp(String mobileotp) {
        this.mobileotp = mobileotp;
    }

    public String getEmailotp() {
        return emailotp;
    }

    public void setEmailotp(String emailotp) {
        this.emailotp = emailotp;
    }

}
