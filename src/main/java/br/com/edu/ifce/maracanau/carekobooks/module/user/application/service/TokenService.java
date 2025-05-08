package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.auth.AuthMissingRefreshTokenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.cookie.extractor.CookieExtractor;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.cookie.factory.CookieFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    @Value("${security.jwt.token.access.expiry-in-seconds}")
    private Integer accessTokenExpiryInSeconds;

    @Value("${security.jwt.token.refresh.expiry-in-seconds}")
    private Integer refreshTokenExpiryInSeconds;

    private final CookieExtractor cookieExtractor;
    private final CookieFactory cookieFactory;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public void accessToken(String username, List<String> roles, HttpServletResponse response) {
        var createdAt = Instant.now();
        var accessExpiresAt = createdAt.plusSeconds(accessTokenExpiryInSeconds);
        var refreshExpiresAt = accessExpiresAt.plusSeconds(refreshTokenExpiryInSeconds);
        response.addCookie(cookieFactory.buildFromAccessToken(getAccessToken(username, roles, createdAt, accessExpiresAt)));
        response.addCookie(cookieFactory.buildFromRefreshToken(getRefreshToken(username, roles, createdAt, refreshExpiresAt)));
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        var cookie = cookieExtractor.extractRefreshToken(request);
        if (cookie == null) {
            throw new AuthMissingRefreshTokenException();
        }

        var jwt = jwtDecoder.decode(cookie);
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
