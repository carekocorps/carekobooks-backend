package br.com.edu.ifce.maracanau.carekobooks.integration.common.provider;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakProvider;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KeycloakAuthProvider {

    private Keycloak keycloak;

    @Autowired
    private KeycloakProvider keycloakProvider;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${keycloak.admin.password}")
    private String password;

    public synchronized Keycloak getKeycloak() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder
                    .builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
                    .grantType(OAuth2Constants.PASSWORD)
                    .build();
        }

        return keycloak;
    }

    public HttpHeaders getAuthorizationHeader() {
        var headers = new HttpHeaders();
        var accessToken = getKeycloak().tokenManager().getAccessToken().getToken();
        headers.setBearerAuth(accessToken);
        return headers;
    }

    public void tearDown() {
        var usersResource = keycloakProvider.getUsersResource();
        for (var user : usersResource.list()) {
            var isAdmin = usersResource
                    .get(user.getId())
                    .roles()
                    .realmLevel()
                    .listEffective()
                    .stream()
                    .anyMatch(x -> x.getName().equalsIgnoreCase("admin"));

            if (!isAdmin) {
                usersResource.get(user.getId()).remove();
                log.info("User {} has been removed", user.getUsername());
            }
        }
    }

}
