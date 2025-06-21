package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.UserRepresentationFactory;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
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
        assertThatThrownBy(() -> keycloakService.signUp(signUpRequest)).isInstanceOf(KeycloakConflictException.class);
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
        assertThatThrownBy(() -> keycloakService.signUp(signUpRequest)).isInstanceOf(KeycloakBadRequestException.class);
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
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(signUpRequest.getUsername());
        assertThat(result.getEmail()).isEqualTo(signUpRequest.getEmail());
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
        assertThatThrownBy(() -> keycloakService.resetVerificationEmail(userRepresentationUUID)).isInstanceOf(KeycloakBadGatewayException.class);
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
        assertThatThrownBy(() -> keycloakService.resetVerificationEmail(userRepresentationUUID)).isInstanceOf(UserAlreadyVerifiedException.class);
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
        assertThatCode(() -> keycloakService.resetVerificationEmail(userRepresentationUUID)).doesNotThrowAnyException();
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
        assertThatThrownBy(() -> keycloakService.changeEmail(userRepresentationUUID)).isInstanceOf(KeycloakNotFoundException.class);
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
        assertThatCode(() -> keycloakService.changeEmail(userRepresentationUUID)).doesNotThrowAnyException();
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
        assertThatThrownBy(() -> keycloakService.update(userRepresentationUUID, updateRequest)).isInstanceOf(KeycloakForbiddenException.class);
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
        assertThatCode(() -> keycloakService.update(userRepresentationUUID, updateRequest)).doesNotThrowAnyException();
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
        assertThatThrownBy(() -> keycloakService.delete(userRepresentationUUID)).isInstanceOf(KeycloakNotFoundException.class);
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
        assertThatCode(() -> keycloakService.delete(userRepresentationUUID)).doesNotThrowAnyException();
    }

}
