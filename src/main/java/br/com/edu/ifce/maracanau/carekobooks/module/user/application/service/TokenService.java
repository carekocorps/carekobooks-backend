package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    private static final long EXPIRATION_TIME_IN_SECONDS = 3600L;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public TokenResponse accessToken(String username, List<String> roles) {
        var createdAt = Instant.now();
        var accessExpiresAt = createdAt.plusSeconds(EXPIRATION_TIME_IN_SECONDS);
        var refreshExpiresAt = accessExpiresAt.plusSeconds(EXPIRATION_TIME_IN_SECONDS * 3);
        var response = new TokenResponse();
        response.setUsername(username);
        response.setIsAuthenticated(true);
        response.setCreatedAt(LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()));
        response.setAccessExpiresAt(LocalDateTime.ofInstant(accessExpiresAt, ZoneId.systemDefault()));
        response.setRefreshExpiresAt(LocalDateTime.ofInstant(refreshExpiresAt, ZoneId.systemDefault()));
        response.setAccessToken(getAccessToken(username, roles, createdAt, accessExpiresAt));
        response.setRefreshToken(getRefreshToken(username, roles, createdAt, refreshExpiresAt));
        return response;
    }

    public TokenResponse refreshToken(String refreshToken) {
        var jwt = jwtDecoder.decode(refreshToken);
        var roles = Optional.ofNullable(jwt.getClaimAsStringList("roles")).orElse(List.of());
        return accessToken(jwt.getSubject(), roles);
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
