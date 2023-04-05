package com.example.spring_boot_pracrice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Slf4j
@Component
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    public Oauth2AuthenticationSuccessHandler() {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String requestUrl = (String) request.getSession().getAttribute("requestUrl");
        if (requestUrl != null) {
            request.getSession().removeAttribute("requestUrl");
            response.sendRedirect(requestUrl);
        } else {
            response.sendRedirect("/user");
        }
    }
}