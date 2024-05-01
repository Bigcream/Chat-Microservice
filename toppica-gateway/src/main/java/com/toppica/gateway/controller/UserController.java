package com.toppica.gateway.controller;

import com.toppica.gateway.dto.User;
import com.toppica.gateway.dto.UserInfo;
import com.toppica.gateway.dto.UserToken;
import com.toppica.gateway.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController extends BaseController{
    private final UserServiceImpl userService;
    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie accessTokenCookie = ResponseCookie.fromClientResponse("access_token", "")
                .maxAge(0)
                .httpOnly(false)
                .path("/")
                .domain("localhost")
                .secure(false)// should be true in production
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.fromClientResponse("refresh_token", "")
                .maxAge(0)
                .httpOnly(false)
                .path("/")
                .domain("localhost")
                .secure(false)// should be true in production
                .build();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Set-Cookie", accessTokenCookie.toString());
        headers.add("Set-Cookie", refreshTokenCookie.toString());

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<UserToken> refreshToken(@RequestBody User user) {
        Mono<UserToken> userInfo = userService.refreshToken(user);
        UserToken userData = userInfo.share().block();
        assert userData != null;
        userData.setEmail(user.getEmail());
        ResponseCookie accessTokenCookie = ResponseCookie.fromClientResponse("access_token", userData.getAccess_token())
                .maxAge(3600)
                .httpOnly(false)
                .path("/")
                .domain("localhost")
                .secure(false)// should be true in production
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(userData);
    }

    @PostMapping("/login")
    public ResponseEntity<Mono<UserToken>> login(@RequestBody User user) {
        Mono<UserToken> userInfo = userService.login(user);
        UserToken userData = userInfo.share().block();
        assert userData != null;
        userData.setEmail(user.getEmail());
        ResponseCookie accessTokenCookie = ResponseCookie.fromClientResponse("access_token", userData.getAccess_token())
                .maxAge(3600)
                .httpOnly(false)
                .path("/")
                .domain("localhost")
                .secure(false)// should be true in production
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.fromClientResponse("refresh_token", userData.getRefresh_token())
                .maxAge(3600)
                .httpOnly(false)
                .path("/")
                .domain("localhost")
                .secure(false)// should be true in production
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", accessTokenCookie.toString())
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(Mono.just(userData));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody User user) {
        userService.register(user);
        return Mono.just(new ResponseEntity<>("Success!", noCacheHeader, HttpStatus.OK));
    }

    @GetMapping("/user-info/{email}")
    public ResponseEntity<Mono<UserInfo>> getUserInfo(@PathVariable String email){
        return new ResponseEntity<>(Mono.just(userService.getUserInfoByEmail(email)), noCacheHeader, HttpStatus.OK);
    }

    @PutMapping("/user-info")
    public ResponseEntity<UserRepresentation> getUserInfo(@RequestBody UserInfo userInfo) {
        UserRepresentation response = userService.updateUserInfo(userInfo);
        return ResponseEntity.ok()
                .body(response);
    }
}
