package com.arch.ihcd.gateway.response;

import com.arch.ihcd.gateway.bean.Privilege;
import com.arch.ihcd.gateway.bean.ResourceServerAccessData;
import com.arch.ihcd.gateway.bean.Role;

import java.util.Set;

public class UserSignInResponse {
    private String id;
    private String username;
    private String email;
    private String mobile;
    private String token;
    private String fullname;

    private Set<Role> roles;
    private Set<Privilege> privileges;
    private Set<ResourceServerAccessData> resourceServerTokens;
    public UserSignInResponse( String id,
                               String username,
                               String email,
                               String mobile,
                               String token,
                               Set<Role> roles,
                               Set<Privilege> privileges,
                               Set<ResourceServerAccessData> resourceServerTokens,String fullname){
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.token = token;
        this.roles = roles;
        this.privileges = privileges;
        this.resourceServerTokens = resourceServerTokens;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public Set<ResourceServerAccessData> getResourceServerTokens() {
        return resourceServerTokens;
    }

    public String getFullname() {
        return fullname;
    }
}
