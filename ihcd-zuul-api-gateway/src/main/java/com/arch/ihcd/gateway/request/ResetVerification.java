package com.arch.ihcd.gateway.request;

public class ResetVerification {
    private boolean mobile;
    private boolean email;
    private boolean qrcode;

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isQrcode() {
        return qrcode;
    }

    public void setQrcode(boolean qrcode) {
        this.qrcode = qrcode;
    }
}
