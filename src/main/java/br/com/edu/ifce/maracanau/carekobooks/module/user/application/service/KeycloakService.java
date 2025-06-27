package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateUsernameRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.enums.KeycloakExceptionStrategy;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserAlreadyVerifiedException;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeycloakService {

    private final KeycloakProvider keycloakProvider;
    private final KeycloakUserMapper keycloakUserMapper;

    public UserRepresentation signUp(UserSignUpRequest request) {
        try {
            var resource = keycloakProvider.getUsersResource();
            try (var response = resource.create(keycloakUserMapper.toRepresentation(request))) {
                if (response.getStatus() != HttpStatus.SC_CREATED) {
                    throw KeycloakExceptionStrategy.of(response.getStatus());
                }

                var userLocationHeader = response.getHeaderString("Location");
                var userId = userLocationHeader.substring(userLocationHeader.lastIndexOf("/") + 1);
                var userResource = resource.get(userId);

                userResource.sendVerifyEmail();
                return userResource.toRepresentation();
            }
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

    public void resetVerificationEmail(UUID keycloakId) {
        try {
            var userResource = keycloakProvider.getUsersResource().get(keycloakId.toString());
            var userRepresentation = userResource.toRepresentation();

            if (Boolean.TRUE.equals(userRepresentation.isEmailVerified())) {
                throw new UserAlreadyVerifiedException();
            }

            userResource.sendVerifyEmail();
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

    public void changeEmail(UUID keycloakId) {
        try {
            keycloakProvider
                    .getUsersResource()
                    .get(keycloakId.toString())
                    .executeActionsEmail(List.of("UPDATE_EMAIL"));
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

    public void change2FA(UUID keycloakId, boolean enable2FA) {
        try {
            var userResource = keycloakProvider.getUsersResource().get(keycloakId.toString());
            var representation = new UserRepresentation();

            if (!enable2FA) {
                userResource
                        .credentials()
                        .stream()
                        .filter(credential -> credential.getType().equalsIgnoreCase("totp"))
                        .forEach(credential -> userResource.removeCredential(credential.getId()));

                representation.setRequiredActions(List.of());
            } else {
                representation.setRequiredActions(List.of("CONFIGURE_TOTP"));
            }

            userResource.update(representation);
            userResource.logout();
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

    public void update(UUID keycloakId, UserUpdateUsernameRequest request) {
        try {
            var userResource = keycloakProvider.getUsersResource().get(keycloakId.toString());
            userResource.update(keycloakUserMapper.toRepresentation(request));
            userResource.logout();
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

    public void delete(UUID keycloakId) {
        try {
            keycloakProvider
                    .getUsersResource()
                    .get(keycloakId.toString())
                    .remove();
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

}
