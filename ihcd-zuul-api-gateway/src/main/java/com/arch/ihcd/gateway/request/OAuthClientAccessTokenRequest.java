package com.arch.ihcd.gateway.request;

public class OAuthClientAccessTokenRequest {

    private String username;
    private String servername;
    private String grantType; // "authorization_code" or "refresh_token"
    private String code; //grantType => "authorization_code"
    private String refreshToken; //grantType => ""refresh_token"

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
