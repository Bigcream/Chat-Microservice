package com.toppica.gateway.service.impl;

import com.toppica.gateway.constant.OAuth2Constant;
import com.toppica.gateway.dto.User;
import com.toppica.gateway.dto.UserInfo;
import com.toppica.gateway.dto.UserToken;
import com.toppica.gateway.exception.GatewayException;
import com.toppica.gateway.service.UserService;
import com.toppica.gateway.util.OAuth2ClientUtil;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.toppica.gateway.constant.OAuth2Constant.REALM_NAME;
import static com.toppica.gateway.constant.OAuth2Constant.URL_LOGIN;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final OAuth2ClientUtil oAuth2ClientUtil;
    private final Keycloak keycloak;
    private final WebClient webClientKeycloakApi;
    private final String USER_ROLE = "user";

    @Value(value = "${keycloak-url}")
    private String KEYCLOAK_URL;

    @Override
    public Mono<UserToken> login(User user) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(OAuth2Constants.CLIENT_ID, OAuth2Constant.CLIENT.getValue());
        formData.add(OAuth2Constants.USERNAME, user.getEmail());
        formData.add(OAuth2Constants.PASSWORD, user.getPassword());
        formData.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD);
        formData.add(OAuth2Constants.CLIENT_SECRET, oAuth2ClientUtil.getClientSecret());
        return webClientKeycloakApi.post()
                .uri(KEYCLOAK_URL + URL_LOGIN.getValue())
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(
                        HttpStatus::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(GatewayException::new))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(GatewayException::new))
                .bodyToMono(UserToken.class);
    }

    @Override
    public Mono<UserToken> refreshToken(User user) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(OAuth2Constants.CLIENT_ID, OAuth2Constant.CLIENT.getValue());
        formData.add(OAuth2Constants.REFRESH_TOKEN, user.getRefresh_token());
        formData.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN);
        formData.add(OAuth2Constants.CLIENT_SECRET, oAuth2ClientUtil.getClientSecret());
        return webClientKeycloakApi.post()
                .uri(KEYCLOAK_URL + URL_LOGIN.getValue())
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(GatewayException::new))
                .bodyToMono(UserToken.class);
    }

    @Override
    public void register(User userDTO) {
        CredentialRepresentation credential = createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getFirstName() + userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        List<RoleRepresentation> rolesResource = keycloak.realm(REALM_NAME.getValue()).roles().list().stream()
                .filter(roleRepresentation -> !roleRepresentation.getName().equals(USER_ROLE))
                .collect(Collectors.toList());
        Response response = keycloak.realm(REALM_NAME.getValue()).users().create(user);
        String userId = getCreatedId(response);
        keycloak.realm(REALM_NAME.getValue()).users().get(userId).roles().realmLevel().add(rolesResource);
    }

    @Override
    public UserInfo getUserInfoByEmail(String email) {
        List<UserRepresentation> userResource = keycloak.realm(REALM_NAME.getValue()).users().searchByEmail(email, true);
        if(userResource.isEmpty()){
            return new UserInfo();
        }
        UserRepresentation userRepresentation = userResource.get(0);
        return UserInfo.builder()
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .id(userRepresentation.getId())
                .build();
    }

    @Override
    public UserRepresentation updateUserInfo(UserInfo userInfo) {
        List<UserRepresentation> userResource = keycloak.realm(REALM_NAME.getValue())
                .users()
                .searchByEmail(userInfo.getEmail(), true);
        if(userResource.isEmpty()){
            return new UserRepresentation();
        }
        UserRepresentation userRepresentation = userResource.get(0);
        userRepresentation.setFirstName(userInfo.getFirstName());
        userRepresentation.setLastName(userInfo.getLastName());
        userRepresentation.setUsername(userInfo.getUsername());
        keycloak.realm(REALM_NAME.getValue()).users().get(userRepresentation.getId()).update(userRepresentation);
        return userRepresentation;
    }

    public CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private String getCreatedId(Response response) {
        URI location = response.getLocation();
        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new WebApplicationException("Username or Email already exist", response);
        } else if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            Response.StatusType statusInfo = response.getStatusInfo();
            throw new WebApplicationException("Create method returned status " +
                    statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode() + "); expected status: Created (201)", response);
        }
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
