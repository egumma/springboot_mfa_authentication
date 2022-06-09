package com.arch.ihcd.gateway.security;

import com.arch.ihcd.gateway.config.JwtProperties;
import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.exception.OAuthClientGetAccessTokenException;
import com.arch.ihcd.gateway.exception.UserNotFoundException;
import com.arch.ihcd.gateway.repository.UserRepository;
import com.auth0.jwt.JWT;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization header, where the JWT token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            System.out.println("---->AuthFilter: Token Not Found ");
            chain.doFilter(request, response);
            return;
        }
        System.out.println("---->AuthFilter: Token Found ");
        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");
        String tokenUserId = request.getHeader(JwtProperties.HEADER_LOGIN_USER)!=null ? request.getHeader(JwtProperties.HEADER_LOGIN_USER) : "";

        if (token != null) {
            // parse the token and validate it
            String userName = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();

            // Search in the DB if we find the user by token subject (username)
            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            if (userName != null) {
                User user = userRepository.findByUsername(userName);
                UserPrincipal principal = new UserPrincipal(user);
                /**
                 * Though token is valid, it has to be exist in database
                 */
                //ToDo: enable this and make sure presign related APIS should not fail
                if(user!=null && !token.equals(user.getSigninToken())){
                    System.out.println("---->AuthFilter: Token is valid but expired");
                    throw new UserNotFoundException("Token is invalid/expired");
                } else if(StringUtils.isNotBlank(tokenUserId) && !tokenUserId.equals(user.getId())) {
                    throw new UserNotFoundException("Header userid & Token user id not matched");
                }
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, null, principal.getAuthorities());
                return auth;
            }
            System.out.println("---->AuthFilter: Token usernot available");
            return null;
        }
        System.out.println("---->AuthFilter: Token not found in request");
        return null;
    }
}
