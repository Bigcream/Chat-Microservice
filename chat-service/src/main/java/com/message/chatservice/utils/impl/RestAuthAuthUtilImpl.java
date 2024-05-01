package com.message.chatservice.utils.impl;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.exception.ErrorType;
import com.message.chatservice.model.dto.UserInfo;
import com.message.chatservice.utils.restemplate.RestAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestAuthAuthUtilImpl implements RestAuthUtil {
    private final RestTemplate restTemplate;

    @Value("${gate-way-url}")
    private String GATE_WAY_URL;
    private static final String INFO_USER_URL = "/api/v1/user-info/";
    private static final String UPDATE_INFO_USER_URL = "/api/v1/user-info";
    @Override
    public UserInfo getUserFromAuthService(String email, String token) {
        ResponseEntity<UserInfo> response = restTemplate.exchange(GATE_WAY_URL + INFO_USER_URL + email,
                HttpMethod.GET, new HttpEntity<>(generateHeaderJson(token)), UserInfo.class);
        if (!response.hasBody()){
            throw new RuntimeException();
        }
        return response.getBody();
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo, String token) {
        ResponseEntity<UserInfo> response = restTemplate.exchange(GATE_WAY_URL + UPDATE_INFO_USER_URL,
                HttpMethod.PUT, new HttpEntity<>(userInfo, generateHeaderJson(token)), UserInfo.class);
        if (!response.hasBody()){
            throw new RuntimeException();
        }
        return response.getBody();
    }

    public HttpHeaders generateHeaderFromData(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public HttpHeaders generateHeaderJson(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token);
        return headers;
    }
}
