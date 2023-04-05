package com.example.spring_boot_pracrice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    Oauth2AuthenticationSuccessHandler handler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeRequests()
//                    .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/login").authenticated()
                    .anyRequest().permitAll().
                and()
                .oauth2Login()
                .successHandler(handler);

            return http.build();
        }

}