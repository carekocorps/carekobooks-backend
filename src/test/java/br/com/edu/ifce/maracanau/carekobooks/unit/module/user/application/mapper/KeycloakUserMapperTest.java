package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.KeycloakUserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class KeycloakUserMapperTest {

    private KeycloakUserMapper keycloakUserMapper;

    @BeforeEach
    void setUp() {
        keycloakUserMapper = Mappers.getMapper(KeycloakUserMapper.class);
    }

    @Test
    void toRepresentation_withNullUpdateRequest_shouldReturnNullRepresentation() {
        // Arrange
        UserUpdateRequest updateRequest = null;

        // Act
        var result = keycloakUserMapper.toRepresentation(updateRequest);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void toRepresentation_withValidUpdateRequest_shouldReturnRepresentation() {
        // Arrange
        var updateRequest = UserUpdateRequestFactory.validRequest();

        // Act
        var result = keycloakUserMapper.toRepresentation(updateRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(updateRequest.getUsername());
    }

}
