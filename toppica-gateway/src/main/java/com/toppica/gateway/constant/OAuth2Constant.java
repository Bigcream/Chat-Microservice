package com.toppica.gateway.constant;

import lombok.Getter;

public enum OAuth2Constant {
    CLIENT("authen-service"),
    GRANT_TYPE_PASSWORD("password"),
    URL_LOGIN("/realms/ToppicaMessage/protocol/openid-connect/token"),
    REALM_NAME("ToppicaMessage"),
    ISSUER_LOCATION("/realms/ToppicaMessage"),
    UPDATE_USER_URL("/admin/realms/ToppicaMessage/users/");

    @Getter
    private String value;

    OAuth2Constant(String value) {
        this.value = value;
    }
}

