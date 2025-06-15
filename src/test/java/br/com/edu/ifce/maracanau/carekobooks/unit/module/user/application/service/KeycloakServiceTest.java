package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.response.UserRepresentationFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.KeycloakService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserAlreadyVerifiedException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @Mock
    private Response response;

    @Mock
    private UserResource userResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private KeycloakProvider keycloakProvider;

    @Mock
    private KeycloakUserMapper keycloakUserMapper;

    @InjectMocks
    private KeycloakService keycloakService;

    @Test
    void signUp_withKeycloakWebApplicationConflictException_shouldThrowKeycloakConflictException() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation(signUpRequest);

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(keycloakUserMapper.toRepresentation(signUpRequest))
                .thenReturn(userRepresentation);

        when(usersResource.create(userRepresentation))
                .thenThrow(new WebApplicationException(Response.status(Response.Status.CONFLICT).build()));

        // Act & Assert
        assertThrows(KeycloakConflictException.class, () -> keycloakService.signUp(signUpRequest));
    }

    @Test
    void signUp_withValidSignUpRequestAndStatusFailure_shouldThrowException() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation(signUpRequest);

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(keycloakUserMapper.toRepresentation(signUpRequest))
                .thenReturn(userRepresentation);

        when(usersResource.create(userRepresentation))
                .thenReturn(response);

        when(response.getStatus())
                .thenReturn(HttpStatus.SC_BAD_REQUEST);

        // Act && Assert
        assertThrows(KeycloakBadRequestException.class, () -> keycloakService.signUp(signUpRequest));
    }

    @Test
    void signUp_withValidSignUpRequest_shouldReturnUserRepresentation() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation(signUpRequest);

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(keycloakUserMapper.toRepresentation(signUpRequest))
                .thenReturn(userRepresentation);

        when(usersResource.create(userRepresentation))
                .thenReturn(response);

        when(response.getStatus())
                .thenReturn(HttpStatus.SC_CREATED);

        when(usersResource.searchByUsername(signUpRequest.getUsername(), true))
                .thenReturn(List.of(userRepresentation));

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        doNothing()
                .when(userResource)
                .sendVerifyEmail();

        // Act
        var result = keycloakService.signUp(signUpRequest);

        // Assert
        assertNotNull(result);
        assertEquals(signUpRequest.getUsername(), result.getUsername());
        assertEquals(signUpRequest.getEmail(), result.getEmail());
    }

    @Test
    void resetVerificationEmail_withKeycloakWebApplicationInternalServerErrorException_shouldThrowKeycloakBadGatewayException() {
        // Arrange
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenThrow(new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()));

        // Act & Assert
        assertThrows(KeycloakBadGatewayException.class, () -> keycloakService.resetVerificationEmail(userRepresentationUUID));
    }

    @Test
    void resetVerificationEmail_withValidKeycloakIdAndEmailVerified_shouldFail() {
        // Arrange
        var userRepresentation = UserRepresentationFactory.validRepresentationWithEmailVerified();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        when(userResource.toRepresentation())
                .thenReturn(userRepresentation);

        // Act && Assert
        assertThrows(UserAlreadyVerifiedException.class, () -> keycloakService.resetVerificationEmail(userRepresentationUUID));
    }

    @Test
    void resetVerificationEmail_withValidKeycloakIdAndEmailNotVerified_shouldSucceed() {
        // Arrange
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        when(userResource.toRepresentation())
                .thenReturn(userRepresentation);

        doNothing()
                .when(userResource)
                .sendVerifyEmail();

        // Act && Assert
        assertDoesNotThrow(() -> keycloakService.resetVerificationEmail(userRepresentationUUID));
    }

    @Test
    void changeEmail_withKeycloakWebApplicationNotFoundException_shouldThrowKeycloakNotFoundException() {
        // Arrange
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenThrow(new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build()));

        // Act & Assert
        assertThrows(KeycloakNotFoundException.class, () -> keycloakService.changeEmail(userRepresentationUUID));
    }

    @Test
    void changeEmail_withValidKeycloakId_shouldSucceed() {
        // Arrange
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        doNothing()
                .when(userResource)
                .executeActionsEmail(ArgumentMatchers.any());

        // Act && Assert
        assertDoesNotThrow(() -> keycloakService.changeEmail(userRepresentationUUID));
    }

    @Test
    void update_withKeycloakWebApplicationForbiddenException_shouldThrowKeycloakForbiddenException() {
        // Arrange
        var updateRequest = UserUpdateRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        when(keycloakUserMapper.toRepresentation(updateRequest))
                .thenReturn(userRepresentation);

        doThrow(new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build()))
                .when(userResource)
                .update(userRepresentation);

        // Act & Assert
        assertThrows(KeycloakForbiddenException.class, () -> keycloakService.update(userRepresentationUUID, updateRequest));
    }

    @Test
    void update_withValidKeycloakIdAndValidUserUpdateRequest_shouldSucceed() {
        // Arrange
        var updateRequest = UserUpdateRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        when(keycloakUserMapper.toRepresentation(updateRequest))
                .thenReturn(userRepresentation);

        doNothing()
                .when(userResource)
                .update(userRepresentation);

        // Act && Assert
        assertDoesNotThrow(() -> keycloakService.update(userRepresentationUUID, updateRequest));
    }

    @Test
    void delete_withKeycloakWebApplicationNotFoundException_shouldThrowKeycloakNotFoundException() {
        // Arrange
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        doThrow(new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build()))
                .when(userResource)
                .remove();

        // Act & Assert
        assertThrows(KeycloakNotFoundException.class, () -> keycloakService.delete(userRepresentationUUID));
    }

    @Test
    void delete_withValidKeycloakId_shouldSucceed() {
        // Arrange
        var userRepresentation = UserRepresentationFactory.validRepresentation();
        var userRepresentationUUID = UUID.fromString(userRepresentation.getId());

        when(keycloakProvider.getUsersResource())
                .thenReturn(usersResource);

        when(usersResource.get(userRepresentation.getId()))
                .thenReturn(userResource);

        doNothing()
                .when(userResource)
                .remove();

        // Act && Assert
        assertDoesNotThrow(() -> keycloakService.delete(userRepresentationUUID));
    }

}
