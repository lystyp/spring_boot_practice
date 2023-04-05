package com.example.spring_boot_pracrice.controller;

import com.example.spring_boot_pracrice.Utils.OAuth2AuthorizedClientProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private OAuth2AuthorizedClientProvider oauth2AuthorizedClientProvider;

    @GetMapping("/user")
    public String getUser(Model model, HttpServletRequest request) {
        if (oauth2AuthorizedClientProvider.getClient() == null) {
            HttpSession session = request.getSession();
            session.setAttribute("requestUrl", "/user");
            return "redirect:/login";
        }

        String token = oauth2AuthorizedClientProvider.getClient().getAccessToken().getTokenValue();
        log.info("Token = " + token);
        if (oauth2AuthorizedClientProvider.getClient().getClientRegistration().getClientName().equals("Google")) {
            log.info("Login by google.");
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String url = "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses,photos";
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            log.info(response.getBody());
            try {
                JSONObject obj = new JSONObject(response.getBody());
                String name = obj.getJSONArray("names").getJSONObject(0).getString("displayName");
                String email = obj.getJSONArray("emailAddresses").getJSONObject(0).getString("value");
                String pictureUrl = obj.getJSONArray("photos").getJSONObject(0).getString("url");
                model.addAttribute("name", name);
                model.addAttribute("email", email);
                model.addAttribute("pictureUrl", pictureUrl);
                model.addAttribute("hasPages", false);
            } catch (Exception e) {
                log.info("Error : " + e.toString());
                return e.toString();
            }

        } else if (oauth2AuthorizedClientProvider.getClient().getClientRegistration().getClientName().equals("Facebook")) {
            log.info("Login by Facebook.");
//            Facebook facebook = new FacebookTemplate(token);
//            PagedList<Page> likes = facebook.fetchConnections("me", "likes", Page.class);
//            for (Page like : likes) {
//                System.out.println(like.getName());
//            }

            // FacebookTemplate不知道為什麼會400，可以改用url打api
            RestTemplate restTemplate = new RestTemplate();
            String infoUrl = "https://graph.facebook.com/v16.0/me?fields=id%2Cname%2Cemail%2Cabout%2Clikes%2Cpicture&access_token="
                    + token;
            String response = restTemplate.getForObject( URI.create(infoUrl), String.class);
            log.debug("Response : " + response);
            try {
                JSONObject obj = new JSONObject(response);
                String name = obj.getString("name");
                String email = obj.getString("email");
                String pictureUrl = obj.getJSONObject("picture").getJSONObject("data").getString("url");
                JSONArray pages = obj.getJSONObject("likes").getJSONArray("data");
                String[] pagesStr = new String[pages.length()];
                for (int i = 0; i < pages.length(); i++) {
                    pagesStr[i] = pages.getJSONObject(i).getString("name");
                }
                model.addAttribute("name", name);
                model.addAttribute("email", email);
                model.addAttribute("pictureUrl", pictureUrl);
                model.addAttribute("pages", pagesStr);
                model.addAttribute("hasPages", true);
            } catch (Exception e) {
                log.info("Error : " + e.toString());
                return e.toString();
            }
        }

        return "user";
    }



}