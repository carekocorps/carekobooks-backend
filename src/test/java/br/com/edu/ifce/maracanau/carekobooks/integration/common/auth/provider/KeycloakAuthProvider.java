package br.com.edu.ifce.maracanau.carekobooks.integration.common.auth.provider;

import br.com.edu.ifce.maracanau.carekobooks.integration.common.auth.token.KeycloakToken;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Component
public class KeycloakAuthProvider {

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${keycloak.admin.password}")
    private String password;

    public HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
        return headers;
    }

    private String getToken() {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("client_id", Collections.singletonList(clientId));
        map.put("client_secret", Collections.singletonList(clientSecret));
        map.put("username", Collections.singletonList(username));
        map.put("password", Collections.singletonList(password));
        map.put("grant_type", Collections.singletonList(OAuth2Constants.PASSWORD));

        var request = new HttpEntity<>(map, httpHeaders);
        var token = restTemplate.postForObject(getTokenUri(), request, KeycloakToken.class);

        assert token != null;
        return token.getAccessToken();
    }

    private String getTokenUri() {
        return UriComponentsBuilder
                .fromUriString(issuerUri)
                .pathSegment("protocol", "openid-connect", "token")
                .build()
                .toUriString();
    }

}
