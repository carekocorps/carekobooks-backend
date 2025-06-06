package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserAlreadyVerifiedException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.keycloak.representations.idm.AbstractUserRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class KeycloakService {

    private final KeycloakProvider keycloakProvider;
    private final KeycloakUserMapper keycloakUserMapper;

    public UserRepresentation signUp(UserSignUpRequest request) {
        var resource = keycloakProvider.getUsersResource();
        var response = resource.create(keycloakUserMapper.toRepresentation(request));
        if (response.getStatus() != HttpStatus.SC_CREATED) {
            throw switch (response.getStatus()) {
                case HttpStatus.SC_BAD_REQUEST -> new KeycloakBadRequestException();
                case HttpStatus.SC_CONFLICT -> new KeycloakConflictException();
                case HttpStatus.SC_FORBIDDEN -> new KeycloakForbiddenException();
                default -> new KeycloakBadGatewayException();
            };
        }

        var representation = resource
                .searchByUsername(request.getUsername(), true)
                .stream()
                .filter(Predicate.not(AbstractUserRepresentation::isEmailVerified))
                .findFirst()
                .orElseThrow(KeycloakUserNotFoundException::new);

        resource.get(representation.getId()).sendVerifyEmail();
        return representation;
    }

    public void resetVerificationEmail(UUID keycloakId) {
        var resource = keycloakProvider.getUsersResource();
        var representation = resource
                .get(keycloakId.toString())
                .toRepresentation();

        if (Boolean.TRUE.equals(representation.isEmailVerified())) {
            throw new UserAlreadyVerifiedException();
        }

        resource.get(representation.getId()).sendVerifyEmail();
    }

    public void resetEmail(UUID keycloakId) {
        keycloakProvider
                .getUsersResource()
                .get(keycloakId.toString())
                .executeActionsEmail(List.of("UPDATE_EMAIL"));
    }

    public void update(UUID keycloakId, UserUpdateRequest request) {
        keycloakProvider
                .getUsersResource()
                .get(keycloakId.toString())
                .update(keycloakUserMapper.toRepresentation(request));
    }

    public void delete(UUID keycloakId) {
        keycloakProvider
                .getUsersResource()
                .get(keycloakId.toString())
                .remove();
    }

}
