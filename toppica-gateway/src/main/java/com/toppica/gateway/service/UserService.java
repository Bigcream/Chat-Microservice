package com.toppica.gateway.service;

import com.toppica.gateway.dto.User;
import com.toppica.gateway.dto.UserInfo;
import com.toppica.gateway.dto.UserToken;
import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserToken> login(User user);

    Mono<UserToken> refreshToken(User user);

    void register(User user);

    UserInfo getUserInfoByEmail(String email);

    UserRepresentation updateUserInfo(UserInfo userInfo);

}
