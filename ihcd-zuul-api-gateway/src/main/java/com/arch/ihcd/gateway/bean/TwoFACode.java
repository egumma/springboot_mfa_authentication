package com.arch.ihcd.gateway.bean;

public class TwoFACode {
    private String codeType;
    private String code;
    private long validTime;

    public TwoFACode(String codeType, String code, long validTime){
        this.code = code;
        this.codeType = codeType;
        this.validTime = validTime;
    }

    public String getCodeType() {
        return codeType;
    }

    public String getCode() {
        return code;
    }

    public long getValidTime() {
        return validTime;
    }
}
