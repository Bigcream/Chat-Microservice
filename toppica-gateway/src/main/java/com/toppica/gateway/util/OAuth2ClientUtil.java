package com.toppica.gateway.util;

import com.toppica.gateway.constant.OAuth2Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2ClientUtil {
    private final OAuth2ClientProperties oAuth2ClientProperties;
    public String getClientSecret(){
        return oAuth2ClientProperties.getRegistration().get(OAuth2Constant.CLIENT.getValue()).getClientSecret();
    }

}
