package com.arch.ihcd.gateway.security;

import com.arch.ihcd.gateway.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private UserPrincipalService userPrincipalService;
    private UserRepository userRepository;

    public JwtSecurityConfiguration(UserPrincipalService userPrincipalService, UserRepository userRepository) {
        this.userPrincipalService = userPrincipalService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // remove csrf and state in session because in jwt we do not need them
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // add jwt filters (1. authentication, 2. authorization)
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),  this.userRepository))
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/ihcd-project-manager/api/cforms/getByConcentId/**").permitAll()
                .antMatchers(HttpMethod.GET, "/ihcd-project-manager/api/participants/**").permitAll()
                .antMatchers("/ihcd-project-manager/api/participants/**/pcform").permitAll()
                .antMatchers( "/ihcd-websocket-service/ws/**").permitAll()
                .antMatchers( "**/ihcd-websocket-service/ws/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/specific").permitAll()//ToDo: remove
                .antMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()//For aws loadbalancer
                .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()//For aws loadbalancer
                .antMatchers(HttpMethod.GET, "/api/users/status").permitAll()//For aws loadbalancer
                .antMatchers(HttpMethod.POST, "/api/users/signout").permitAll()//For aws loadbalancer
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                .antMatchers(HttpMethod.POST, "/api/instances").permitAll()
                .antMatchers("/api/users/*/resetverification").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/users/signin").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/presignin").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/*/verifyemail/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/users/*/forgotpassword").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/users/*/reseforgotpassword").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**").permitAll()
                .antMatchers("/api/users/management/*").hasRole("MANAGER")
                .antMatchers("/api/users/admin/*").hasRole("ADMIN")
                .antMatchers("/api/users").hasRole("ADMIN")
                .anyRequest().authenticated();
    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow eureka client to be accessed without authentication
        web.ignoring().antMatchers("/*/")//
                .antMatchers("/eureka/**")//
                .antMatchers(HttpMethod.OPTIONS, "/**") // Request type options should be allowed.
                .antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationManager();
    }


}
