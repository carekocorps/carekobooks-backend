package br.com.edu.ifce.maracanau.carekobooks.integration.common.provider;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.enums.KeycloakExceptionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class KeycloakAuthProvider {

    private Keycloak keycloak;

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

    @Autowired
    private KeycloakProvider keycloakProvider;

    @Autowired
    private KeycloakUserMapper keycloakUserMapper;

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

    public UserRepresentation create(UserSignUpRequest request, boolean verify) {
        var resource = keycloakProvider.getUsersResource();
        try (var response = resource.create(keycloakUserMapper.toRepresentation(request))) {
            if (response.getStatus() != HttpStatus.SC_CREATED) {
                throw KeycloakExceptionStrategy.of(response.getStatus());
            }

            var userLocationHeader = response.getHeaderString("Location");
            var userId = userLocationHeader.substring(userLocationHeader.lastIndexOf("/") + 1);
            var userResource = resource.get(userId);

            if (verify) {
                var credentials = new CredentialRepresentation();
                credentials.setValue(UUID.randomUUID().toString());
                credentials.setType(CredentialRepresentation.PASSWORD);
                credentials.setTemporary(false);

                var representation = new UserRepresentation();
                representation.setCredentials(List.of(credentials));
                representation.setRequiredActions(List.of());
                representation.setEmailVerified(true);
                userResource.update(representation);
            }

            return userResource.toRepresentation();
        }
    }

    public HttpHeaders getAuthorizationHeaders() {
        var headers = new HttpHeaders();
        var accessToken = getKeycloak().tokenManager().getAccessToken().getToken();
        headers.setBearerAuth(accessToken);
        return headers;
    }

    public HttpHeaders getMultipartFormDataAuthorizationHeaders() {
        var headers = getAuthorizationHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
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
