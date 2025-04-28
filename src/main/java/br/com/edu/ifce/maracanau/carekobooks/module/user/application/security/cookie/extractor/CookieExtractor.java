package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.cookie.extractor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieExtractor {

    @Value("${security.jwt.token.access.cookie-name}")
    private String accessTokenCookieName;

    @Value("${security.jwt.token.refresh.cookie-name}")
    private String refreshTokenCookieName;

    public String extractAccessToken(HttpServletRequest request) {
        return extract(accessTokenCookieName, request);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return extract(refreshTokenCookieName, request);
    }

    private static String extract(String cookieName, HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

}
