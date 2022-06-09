package com.arch.ihcd.gateway.config;

import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.repository.UserRepository;
import com.auth0.jwt.JWT;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class UserConfig {

    @Autowired
    private UserRepository userRepository;

    public String getLoginUser() {
        HttpServletRequest currentRequest = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String plainUser = currentRequest.getHeader(JwtProperties.HEADER_LOGIN_USER);
        String tokenUser = getUsernameByToken(currentRequest);
        String username = StringUtils.isNotBlank(plainUser) ?
                plainUser : (StringUtils.isNotBlank(tokenUser)? tokenUser : "SYSTEM");
        System.out.println("---->Gateway user: "+username);
        return username;
    }

    private String getUsernameByToken(HttpServletRequest currentRequest) {
        String token = currentRequest.getHeader(JwtProperties.HEADER_STRING) != null? currentRequest.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"") : "";

        if (StringUtils.isNotBlank(token)) {
            return JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
        }
        return "";

    }
}
