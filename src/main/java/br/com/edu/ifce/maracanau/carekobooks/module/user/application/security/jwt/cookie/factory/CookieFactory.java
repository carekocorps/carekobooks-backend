package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.jwt.cookie.factory;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.TokenService;
import jakarta.servlet.http.Cookie;

public class CookieFactory {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private CookieFactory() {
    }

    public static Cookie buildFromAccessToken(String accessToken) {
        return build(ACCESS_TOKEN_COOKIE_NAME, accessToken, TokenService.ACCESS_TOKEN_EXPIRATION_TIME_IN_SECONDS);
    }

    public static Cookie buildFromRefreshToken(String refreshToken) {
        return build(REFRESH_TOKEN_COOKIE_NAME, refreshToken, TokenService.REFRESH_TOKEN_EXPIRATION_TIME_IN_SECONDS);
    }

    private static Cookie build(String name, String value, Integer expirationTimeInSeconds) {
        var cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(expirationTimeInSeconds);
        return cookie;
    }

}
