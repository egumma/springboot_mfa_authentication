package com.arch.ihcd.gateway.util;

public class AppConstants {


    public static final String TWOFATYPE_MOBILE = "MOBILE";
    public static final String TWOFATYPE_EMAIL = "EMAIL";
    public static final String TWOFATYPE_PROFILE = "PROFILE";
    public static final String TWOFATYPE_QRCODE = "QRCODE";
    public static final boolean IS_QRCODE_DEFAULT_ENABLE = false;
    public static final boolean IS_MOBILE_VERIFY_DEFAULT_ENABLE = false;
    public static final boolean IS_EMAIL_VERIFY_DEFAULT_ENABLE = false;

    public static final long MOBILE_OTP_VALID_TIME_IN_MILLISECONDS = 10*60*1000; //10 Minute
    public static final long EMAIL_OTP_VALID_TIME_IN_MILLISECONDS = (3*1440)*60*1000; //1440 Minutes = 1 day

    public static final String SMS_USER_AC_ACTIVATION = "Dear user, Your iHCD app mobile verification code - ";

    public static final String QR_CODE_ISSUER = "iHCD";
    public static final String EMAIL_IHCD_NOREPLY = "noreply@ihcd.com";
    public static final String USER_ADMIN = "admin";

    public static final String IHCD_FORGOTPASSWORD = "/auth/forgotpassword";

    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_USER_ALREADY_EXIST = "User already exist";



}
