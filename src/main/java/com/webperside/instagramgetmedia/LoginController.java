package com.webperside.instagramgetmedia;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping
public class LoginController {

    private final RestTemplate restTemplate;
    private final String clientId = "2366115863696167";
    private final String clientSecret = "4de71a2b94b68f08369994a88ef59644";
    private final String redirectUrl = "https://instagram-get-media.herokuapp.com/login/code";


    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {

        final String url = "https://www.instagram.com/oauth/authorize?client_id=%s&redirect_uri=%s&scope=user_profile,user_media&response_type=code";
        response.sendRedirect(String.format(url, clientId, redirectUrl));
    }

    @GetMapping("/login/code")
    public Map<?,?> handleCode(@RequestParam("code") String code){

        final String url = "https://api.instagram.com/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("email", "first.last@example.com");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", redirectUrl);
        map.add("code", code);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        return restTemplate.postForObject(url, request, Map.class);
    }

    @GetMapping("/long-lived-token")
    public Map<?,?> getLongLivedToken(@RequestParam("accessToken") String accessToken){
        final String url = "https://graph.instagram.com/access_token" +
                "?grant_type=ig_exchange_token" +
                "&client_secret=%s" +
                "&access_token=%s";

        return restTemplate.getForObject(String.format(url, clientSecret, accessToken), Map.class);
    }

    @GetMapping("/me")
    public Map<?,?> me(@RequestParam("llToken") String llToken){
        final String url = "https://graph.instagram.com/me?fields=id,username&access_token=%s";

        return restTemplate.getForObject(String.format(url, llToken), Map.class);
    }

    @GetMapping("/media")
    public Map<?,?> getMedia(@RequestParam("llToken") String llToken){
        final String url = "https://graph.instagram.com/me/media?fields=id,media_type,media_url,username,timestamp&access_token=%s";

        return restTemplate.getForObject(String.format(url, llToken), Map.class);
    }
}
