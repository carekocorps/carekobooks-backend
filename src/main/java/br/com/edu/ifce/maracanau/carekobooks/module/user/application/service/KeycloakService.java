package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.enums.KeycloakExceptionStrategy;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserAlreadyVerifiedException;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.keycloak.representations.idm.AbstractUserRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

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
            }

            var representation = resource
                    .searchByUsername(request.getUsername(), true)
                    .stream()
                    .filter(Predicate.not(AbstractUserRepresentation::isEmailVerified))
                    .findFirst()
                    .orElseThrow(KeycloakNotFoundException::new);

            resource.get(representation.getId()).sendVerifyEmail();
            return representation;
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

    public void resetVerificationEmail(UUID keycloakId) {
        try {
            var resource = keycloakProvider.getUsersResource();
            var representation = resource
                    .get(keycloakId.toString())
                    .toRepresentation();

            if (Boolean.TRUE.equals(representation.isEmailVerified())) {
                throw new UserAlreadyVerifiedException();
            }

            resource.get(representation.getId()).sendVerifyEmail();
        } catch (WebApplicationException e) {
            log.error(e.getMessage());
            throw KeycloakExceptionStrategy.of(e.getResponse().getStatus());
        }
    }

    public void resetEmail(UUID keycloakId) {
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

    public void update(UUID keycloakId, UserUpdateRequest request) {
        try {
            keycloakProvider
                    .getUsersResource()
                    .get(keycloakId.toString())
                    .update(keycloakUserMapper.toRepresentation(request));
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
