package com.arch.ihcd.gateway.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceServerAccessData {
    private String accessToken;
    private String expiresIn;
    private String refreshToken;
    private String refreshExpiresIn;
    private String tokenType;//bearer or any other type
    private String servername;
    private String scope;
    private String user_id;
    private String team_id;

    public ResourceServerAccessData() {}

    public ResourceServerAccessData(String accessToken,
                                    String expiresIn,String refreshToken,
                                    String refreshExpiresIn,
                                    String tokenType,
                                    String servername, String scope,
                                    String user_id, String team_id) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshExpiresIn = refreshExpiresIn;
        this.tokenType = tokenType;
        this.servername = servername;
        this.user_id = user_id;
        this.team_id = team_id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ResourceServerAccessData) {
            return this.servername.equals(((ResourceServerAccessData)obj).getServername());
        } else return false;
    }
}
