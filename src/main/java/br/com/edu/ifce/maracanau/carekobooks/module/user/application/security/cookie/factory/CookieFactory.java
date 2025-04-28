package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.cookie.factory;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieFactory {

    @Value("${security.jwt.token.access.cookie-name}")
    private String accessTokenCookieName;

    @Value("${security.jwt.token.refresh.cookie-name}")
    private String refreshTokenCookieName;

    @Value("${security.jwt.token.access.expiry-in-seconds}")
    private Integer accessTokenExpiryInSeconds;

    @Value("${security.jwt.token.refresh.expiry-in-seconds}")
    private Integer refreshTokenExpiryInSeconds;

    public Cookie buildFromAccessToken(String accessToken) {
        return build(accessTokenCookieName, accessToken, accessTokenExpiryInSeconds);
    }

    public Cookie buildFromRefreshToken(String refreshToken) {
        return build(refreshTokenCookieName, refreshToken, refreshTokenExpiryInSeconds);
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
