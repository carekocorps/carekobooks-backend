package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.SerializationUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageMapper imageMapper = Mappers.getMapper(ImageMapper.class);

    @InjectMocks
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_withNullKeycloakIdAndNullSignUpRequest_shouldReturnNullUser() {
        // Arrange
        UserSignUpRequest signUpRequest = null;
        UUID keycloakId = null;

        // Act
        var result = userMapper.toEntity(keycloakId, signUpRequest);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void toEntity_withValidKeycloakIdAndNullSignUpRequest_shouldReturnUser() {
        // Arrange
        UserSignUpRequest signUpRequest = null;
        var keycloakId = UUID.randomUUID();

        // Act
        var result = userMapper.toEntity(keycloakId, signUpRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getKeycloakId()).isEqualTo(keycloakId);
    }

    @Test
    void toEntity_withValidKeycloakIdAndValidSignUpRequest_shouldReturnUser() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var keycloakId = UUID.randomUUID();
        var user = UserFactory.validUser(keycloakId, signUpRequest);

        // Act
        var result = userMapper.toEntity(keycloakId, signUpRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getKeycloakId()).isEqualTo(user.getKeycloakId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getDisplayName()).isEqualTo(user.getDisplayName());
        assertThat(result.getDescription()).isEqualTo(user.getDescription());
    }

    @Test
    void toEntity_withNonExistingUserId_shouldReturnNullUser() {
        // Arrange
        var username = UserFactory.validUser().getUsername();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Act
        var result = userMapper.toEntity(username);

        // Assert
        assertThat(result).isNull();
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void toEntity_withExistingUserId_shouldReturnUser() {
        // Arrange
        var user = UserFactory.validUser();

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        // Act
        var result = userMapper.toEntity(user.getUsername());

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void toResponse_withNullUser_shouldReturnNullUserResponse() {
        // Arrange
        User user = null;

        // Act
        var result = userMapper.toResponse(user);

        // Assert
        assertThat(result).isNull();
        verify(imageMapper, never()).toResponse(any(Image.class));
    }

    @Test
    void toResponse_withValidUser_shouldReturnUserResponse() {
        // Arrange
        var user = UserFactory.validUser();
        var userResponse = UserResponseFactory.validResponse(user);

        when(imageMapper.toResponse(user.getImage()))
                .thenReturn(userResponse.getImage());

        // Act
        var result = userMapper.toResponse(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userResponse.getId());
        assertThat(result.getKeycloakId()).isEqualTo(userResponse.getKeycloakId());
        assertThat(result.getUsername()).isEqualTo(userResponse.getUsername());
        assertThat(result.getDisplayName()).isEqualTo(userResponse.getDisplayName());
        assertThat(result.getDescription()).isEqualTo(userResponse.getDescription());
        assertThat(result.getProgressesCount()).isEqualTo(userResponse.getProgressesCount());
        assertThat(result.getReviewsCount()).isEqualTo(userResponse.getReviewsCount());
        assertThat(result.getThreadsCount()).isEqualTo(userResponse.getThreadsCount());
        assertThat(result.getRepliesCount()).isEqualTo(userResponse.getRepliesCount());
        assertThat(result.getFollowingCount()).isEqualTo(userResponse.getFollowingCount());
        assertThat(result.getFollowersCount()).isEqualTo(userResponse.getFollowersCount());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(userResponse.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(userResponse.getUpdatedAt());
        assertThat(Optional.ofNullable(result.getImage()).map(ImageResponse::getId).orElse(null)).isEqualTo(Optional.ofNullable(userResponse.getImage()).map(ImageResponse::getId).orElse(null));
    }

    @Test
    void toSimplifiedResponse_withNullUser_shouldReturnNullSimplifiedUserResponse() {
        // Arrange
        User user = null;

        // Act
        var result = userMapper.toSimplifiedResponse(user);

        // Assert
        assertThat(result).isNull();
        verify(imageMapper, never()).toResponse(any(Image.class));
    }

    @Test
    void toSimplifiedResponse_withValidUser_shouldReturnSimplifiedUserResponse() {
        // Arrange
        var user = UserFactory.validUser();
        var userResponse = SimplifiedUserResponseFactory.validResponse(user);

        when(imageMapper.toResponse(user.getImage()))
                .thenReturn(userResponse.getImage());

        // Act
        var result = userMapper.toSimplifiedResponse(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userResponse.getId());
        assertThat(result.getKeycloakId()).isEqualTo(userResponse.getKeycloakId());
        assertThat(result.getUsername()).isEqualTo(userResponse.getUsername());
        assertThat(result.getDisplayName()).isEqualTo(userResponse.getDisplayName());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(userResponse.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(userResponse.getUpdatedAt());
        assertThat(Optional.ofNullable(result.getImage()).map(ImageResponse::getId).orElse(null)).isEqualTo(Optional.ofNullable(userResponse.getImage()).map(ImageResponse::getId).orElse(null));
    }

    @Test
    void updateEntity_withValidUserAndNullUserUpdateRequest_shouldPreserveUser() {
        // Arrange
        UserUpdateRequest updateRequest = null;
        var user = UserFactory.validUser();
        var newUser = SerializationUtils.clone(user);

        // Act
        userMapper.updateEntity(newUser, updateRequest);

        // Assert
        assertThat(newUser.getId()).isEqualTo(user.getId());
        assertThat(newUser.getKeycloakId()).isEqualTo(user.getKeycloakId());
        assertThat(newUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(newUser.getDisplayName()).isEqualTo(user.getDisplayName());
        assertThat(newUser.getDescription()).isEqualTo(user.getDescription());
        assertThat(newUser.getCreatedAt()).isEqualToIgnoringNanos(user.getCreatedAt());
        assertThat(Optional.ofNullable(newUser.getImage()).map(Image::getId).orElse(null)).isEqualTo(Optional.ofNullable(user.getImage()).map(Image::getId).orElse(null));
    }

    @Test
    void updateEntity_withValidUserAndValidUserUpdateRequest_shouldUpdateUser() {
        // Arrange
        var updateRequest = UserUpdateRequestFactory.validRequest();
        var user = UserFactory.validUser();
        var newUser = SerializationUtils.clone(user);

        // Act
        userMapper.updateEntity(newUser, updateRequest);

        // Assert
        assertThat(newUser.getId()).isEqualTo(user.getId());
        assertThat(newUser.getKeycloakId()).isEqualTo(user.getKeycloakId());
        assertThat(newUser.getUsername()).isEqualTo(updateRequest.getUsername());
        assertThat(newUser.getDisplayName()).isEqualTo(updateRequest.getDisplayName());
        assertThat(newUser.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(newUser.getCreatedAt()).isEqualToIgnoringNanos(user.getCreatedAt());
        assertThat(Optional.ofNullable(newUser.getImage()).map(Image::getId).orElse(null)).isEqualTo(Optional.ofNullable(user.getImage()).map(Image::getId).orElse(null));
    }

}
