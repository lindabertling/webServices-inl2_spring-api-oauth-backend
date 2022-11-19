package com.example.springapioauthbackend_inlamning2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
        throws Exception {
        return http
                .oauth2Login()
                .successHandler(new AuthenticationHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/user/repos")
                .authenticated()
                .antMatchers("/**")
                .permitAll()
                .and()
                .build();
    }
}
