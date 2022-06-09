package com.arch.ihcd.gateway.response;

public class UserPreSignInResponse {
    private String id;
    private String username;
    private String email;
    private String mobile;
    private String token;
    private String fullname;

    //QR code auth fields
    private boolean isQRAuthEnabled = false;
    private boolean isQRSetupdone = false;
    private boolean isQRCodeverified = false;
    private String qrCodeURL = "";

    //Mobile
    private boolean mobileverifyenabled = true;
    private boolean mobileverified = false;
    private long mobileotpvalidtime;
    //email
    private boolean emailverifyenabled = true;
    private boolean emailverified = false;
    private long emailotpvalidtime;


    public UserPreSignInResponse(String id, String username,
                                 String email,
                                 String mobile,
                                 String token,
                                 boolean mobileverifyenabled,
                                 boolean mobileverified,
                                 long mobileotpvalidtime,
                                 boolean emailverifyenabled,
                                 boolean emailverified,
                                 long emailotpvalidtime,
                                 boolean isQRAuthEnabled,
                                 boolean isQRSetupdone,
                                 boolean isQRCodeverified,
                                 String qrCodeURL,
                                 String fullname){
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.token = token;
        this.mobileverifyenabled = mobileverifyenabled;
        this.mobileverified = mobileverified;
        this.mobileotpvalidtime = mobileotpvalidtime;
        this.emailverifyenabled = emailverifyenabled;
        this.emailverified = emailverified;
        this.emailotpvalidtime = emailotpvalidtime;
        this.isQRAuthEnabled = isQRAuthEnabled;
        this.isQRSetupdone = isQRSetupdone;
        this.isQRCodeverified = isQRCodeverified;
        this.qrCodeURL = qrCodeURL;
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getToken() {
        return token;
    }

    public boolean isQRAuthEnabled() {
        return isQRAuthEnabled;
    }

    public boolean isQRSetupdone() {
        return isQRSetupdone;
    }

    public boolean isQRCodeverified() {
        return isQRCodeverified;
    }

    public String getQrCodeURL() {
        return qrCodeURL;
    }

    public boolean isMobileverifyenabled() {
        return mobileverifyenabled;
    }

    public boolean isMobileverified() {
        return mobileverified;
    }

    public boolean isEmailverifyenabled() {
        return emailverifyenabled;
    }

    public boolean isEmailverified() {
        return emailverified;
    }

    public long getMobileotpvalidtime() {
        return mobileotpvalidtime;
    }

    public long getEmailotpvalidtime() {
        return emailotpvalidtime;
    }

    public String getFullname() {
        return fullname;
    }
}
