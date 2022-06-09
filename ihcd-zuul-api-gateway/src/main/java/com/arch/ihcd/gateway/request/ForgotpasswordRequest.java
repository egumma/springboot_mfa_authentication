package com.arch.ihcd.gateway.request;

public class ForgotpasswordRequest {
    private String password;
    private String confirmPassword;
    private String forgototp;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForgototp() {
        return forgototp;
    }

    public void setForgototp(String forgototp) {
        this.forgototp = forgototp;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
