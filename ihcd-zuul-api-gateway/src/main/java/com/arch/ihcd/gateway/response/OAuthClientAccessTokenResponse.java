package com.arch.ihcd.gateway.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class OAuthClientAccessTokenResponse implements Serializable {
    private String accessToken;
    private String expiresIn;
    private String refreshToken;
    private String refreshExpiresIn;
    private String tokenType;//bearer or any other type
    private String servername;
    private String username;
    private String scope;
    private String user_id;
    private String team_id;
}
