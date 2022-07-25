package com.webperside.instagramgetmedia;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final RestTemplate restTemplate;

    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public void login(HttpServletResponse response) throws IOException {
        final String clientId = "2366115863696167";
        final String redirectUrl = "https://instagram-get-media.herokuapp.com/login/code";
        final String url = "https://www.instagram.com/oauth/authorize?client_id=%s&redirect_uri=%s&scope=user_profile,user_media&response_type=code";
        response.sendRedirect(String.format(url, clientId, redirectUrl));
    }

    @GetMapping("/code")
    public String handleCode(@RequestParam("code") String code){

//        System.err.println("Code " + code);
//
//        restTemplate.postForEntity()

        return code;
    }
}
