package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.TokenDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expiration-time-in-ms}")
    private Long expirationTimeInMs;

    private final UserDetailsService userDetailsService;
    private Algorithm algorithm = null;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        var createdAt = new Date();
        var expiresAt = new Date(createdAt.getTime() + expirationTimeInMs);
        var tokenDTO = new TokenDTO();
        tokenDTO.setUsername(username);
        tokenDTO.setIsAuthenticated(true);
        tokenDTO.setCreatedAt(createdAt);
        tokenDTO.setExpiresAt(expiresAt);
        tokenDTO.setAccessToken(getAccessToken(username, roles, createdAt, expiresAt));
        tokenDTO.setRefreshToken(getRefreshToken(username, roles, createdAt));
        return tokenDTO;
    }

    public TokenDTO refreshToken(String refreshToken) {
        var token = getTokenFromBearer(refreshToken).orElse("");
        var decodedJWT = getDecodedJWT(token);
        return createAccessToken(decodedJWT.getSubject(), decodedJWT.getClaim("roles").asList(String.class));
    }

    public String resolveToken(HttpServletRequest request) {
        var bearerToken = request.getHeader("Authorization");
        return getTokenFromBearer(bearerToken).orElse(null);
    }

    public boolean validateToken(String token) {
        try {
            return getDecodedJWT(token).getExpiresAt().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            var decodedJWT = getDecodedJWT(token);
            var userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (Exception ex) {
            return null;
        }
    }

    private String getAccessToken(String username, List<String> roles, Date createdAt, Date expiresAt) {
        var issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(createdAt)
                .withExpiresAt(expiresAt)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    private String getRefreshToken(String username, List<String> roles, Date createdAt) {
        var expiresAt = new Date(createdAt.getTime() + (expirationTimeInMs * 3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(createdAt)
                .withExpiresAt(expiresAt)
                .withSubject(username)
                .sign(algorithm);
    }

    private DecodedJWT getDecodedJWT(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    private Optional<String> getTokenFromBearer(String bearerToken) {
        return StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")
                ? Optional.of(bearerToken.replace("Bearer", "").trim())
                : Optional.empty();
    }

}
