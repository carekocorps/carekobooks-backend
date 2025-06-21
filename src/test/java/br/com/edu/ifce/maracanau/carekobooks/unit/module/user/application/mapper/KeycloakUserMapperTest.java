package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class KeycloakUserMapperTest {

    private KeycloakUserMapper keycloakUserMapper;

    @BeforeEach
    void setUp() {
        keycloakUserMapper = Mappers.getMapper(KeycloakUserMapper.class);
    }

    @Test
    void toRepresentation_withNullSignUpRequest_shouldReturnNullRepresentation() {
        // Arrange
        UserSignUpRequest signUpRequest = null;

        // Act
        var result = keycloakUserMapper.toRepresentation(signUpRequest);

        // Assert
        assertNull(result);
    }

    @Test
    void toRepresentation_withValidSignUpRequest_shouldReturnRepresentation() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();

        // Act
        var result = keycloakUserMapper.toRepresentation(signUpRequest);

        // Assert
        assertNotNull(result);
        assertEquals(signUpRequest.getUsername(), result.getUsername());
        assertEquals(signUpRequest.getEmail(), result.getEmail());
    }

    @Test
    void toRepresentation_withNullUpdateRequest_shouldReturnNullRepresentation() {
        // Arrange
        UserUpdateRequest updateRequest = null;

        // Act
        var result = keycloakUserMapper.toRepresentation(updateRequest);

        // Assert
        assertNull(result);
    }

    @Test
    void toRepresentation_withValidUpdateRequest_shouldReturnRepresentation() {
        // Arrange
        var updateRequest = UserUpdateRequestFactory.validRequest();

        // Act
        var result = keycloakUserMapper.toRepresentation(updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updateRequest.getUsername(), result.getUsername());
    }

}
