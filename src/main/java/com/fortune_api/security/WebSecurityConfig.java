package com.fortune_api.security;

import com.fortune_api.security.jwt.TokenAuhtenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/b_operations/accont/createAccount").authenticated()
                        .requestMatchers("/b_operations/accont/findAccount").authenticated()
                        .requestMatchers("/b_operations/card/findMainCar").authenticated()
                        .requestMatchers("/b_operations/card/findCards").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(new TokenAuhtenticationFilter(), OncePerRequestFilter.class);

        return http.build();
    }
}
