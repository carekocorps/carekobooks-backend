package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.enums.KeycloakExceptionStrategy;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeycloakService {

    private final KeycloakProvider keycloakProvider;
    private final KeycloakUserMapper keycloakUserMapper;

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
