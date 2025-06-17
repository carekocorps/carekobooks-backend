package br.com.edu.ifce.maracanau.carekobooks.integration.common.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
        return headers;
    }

    private String getToken() {
        var restTemplate = new RestTemplate();
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("grant_type", Collections.singletonList(OAuth2Constants.CLIENT_CREDENTIALS));
        map.put("client_id", Collections.singletonList(clientId));
        map.put("client_secret", Collections.singletonList(clientSecret));

        var url = UriComponentsBuilder
                .fromPath(issuerUri)
                .pathSegment("protocol", "openid-connect", "token")
                .build()
                .toUriString();

        var request = new HttpEntity<>(map, httpHeaders);
        var token = restTemplate.postForObject(url, request, KeyCloakToken.class);

        assert token != null;
        return token.accessToken();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {
    }

}
