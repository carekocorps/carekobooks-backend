package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.jwt.cookie.extractor;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.jwt.cookie.factory.CookieFactory;
import jakarta.servlet.http.HttpServletRequest;

public class CookieExtractor {

    private CookieExtractor() {
    }

    public static String extractAccessToken(HttpServletRequest request) {
        return extract(CookieFactory.ACCESS_TOKEN_COOKIE_NAME, request);
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        return extract(CookieFactory.REFRESH_TOKEN_COOKIE_NAME, request);
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
