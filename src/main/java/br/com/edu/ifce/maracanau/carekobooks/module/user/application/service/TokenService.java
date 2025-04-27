package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.jwt.cookie.extractor.CookieExtractor;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.jwt.cookie.factory.CookieFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    public static final Integer ACCESS_TOKEN_EXPIRATION_TIME_IN_SECONDS = 3600;
    public static final Integer REFRESH_TOKEN_EXPIRATION_TIME_IN_SECONDS = ACCESS_TOKEN_EXPIRATION_TIME_IN_SECONDS * 3;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public void accessToken(String username, List<String> roles, HttpServletResponse response) {
        var createdAt = Instant.now();
        var accessExpiresAt = createdAt.plusSeconds(ACCESS_TOKEN_EXPIRATION_TIME_IN_SECONDS);
        var refreshExpiresAt = accessExpiresAt.plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME_IN_SECONDS);

        response.addCookie(CookieFactory.buildFromAccessToken(getAccessToken(username, roles, createdAt, accessExpiresAt)));
        response.addCookie(CookieFactory.buildFromRefreshToken(getRefreshToken(username, roles, createdAt, refreshExpiresAt)));
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        var jwt = jwtDecoder.decode(CookieExtractor.extractRefreshToken(request));
        var roles = Optional.ofNullable(jwt.getClaimAsStringList("roles")).orElse(List.of());
        accessToken(jwt.getSubject(), roles, response);
    }

    private String getAccessToken(String username, List<String> roles, Instant createdAt, Instant expiresAt) {
        var issuer = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        var claims = JwtClaimsSet
                .builder()
                .issuer(issuer)
                .subject(username)
                .issuedAt(createdAt)
                .expiresAt(expiresAt)
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String getRefreshToken(String username, List<String> roles, Instant createdAt, Instant expiresAt) {
        var claims = JwtClaimsSet
                .builder()
                .subject(username)
                .issuedAt(createdAt)
                .expiresAt(expiresAt)
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
