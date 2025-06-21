package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.MultipartFileFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.UserRepresentationFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageUploadException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.KeycloakService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void find_withNonExistingUsername_shouldReturnEmptyUserResponse() {
        // Arrange
        var username = UserFactory.validUser().getUsername();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Act
        var result = userService.find(username);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    void find_withExistingUsername_shouldReturnUserResponse() {
        // Arrange
        var user = UserFactory.validUser();
        var userResponse = UserResponseFactory.validResponse(user);

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        // Act
        var result = userService.find(user.getUsername());

        // Assert
        assertTrue(result.isPresent());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void signUp_withValidSignUpRequestAndValidImageAndInternalServerError_shouldThrowException() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation(signUpRequest);
        var user = UserFactory.validUser(UUID.fromString(userRepresentation.getId()), signUpRequest);
        var multipartFile = MultipartFileFactory.validFile();

        when(keycloakService.signUp(signUpRequest))
                .thenReturn(userRepresentation);

        when(userMapper.toEntity(UUID.fromString(userRepresentation.getId()), signUpRequest))
                .thenReturn(user);

        when(imageService.create(multipartFile))
                .thenThrow(ImageUploadException.class);

        // Act && Assert
        assertThrows(ImageUploadException.class, () -> userService.signUp(signUpRequest, multipartFile));
        verify(keycloakService, times(1)).signUp(signUpRequest);
        verify(userMapper, times(1)).toEntity(UUID.fromString(userRepresentation.getId()), signUpRequest);
        verify(imageService, times(1)).create(multipartFile);
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    void signUp_withValidSignUpRequestAndNullImage_shouldReturnUserResponse() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation(signUpRequest);
        var user = UserFactory.validUser(UUID.fromString(userRepresentation.getId()), signUpRequest);
        var userResponse = UserResponseFactory.validResponse(user);
        MultipartFile multipartFile = null;

        when(keycloakService.signUp(signUpRequest))
                .thenReturn(userRepresentation);

        when(userMapper.toEntity(UUID.fromString(userRepresentation.getId()), signUpRequest))
                .thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        // Act
        var result = userService.signUp(signUpRequest, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse, result);
        verify(keycloakService, times(1)).signUp(signUpRequest);
        verify(userMapper, times(1)).toEntity(UUID.fromString(userRepresentation.getId()), signUpRequest);
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void signUp_withValidSignUpRequestAndValidImage_shouldReturnUserResponse() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = UserRepresentationFactory.validRepresentation(signUpRequest);
        var user = UserFactory.validUser(UUID.fromString(userRepresentation.getId()), signUpRequest);
        var userResponse = UserResponseFactory.validResponse(user);

        var multipartFile = MultipartFileFactory.validFile();
        var image = ImageFactory.validImage(multipartFile);
        var imageResponse = ImageResponseFactory.validResponse(image);

        when(keycloakService.signUp(signUpRequest))
                .thenReturn(userRepresentation);

        when(userMapper.toEntity(UUID.fromString(userRepresentation.getId()), signUpRequest))
                .thenReturn(user);

        when(imageService.create(multipartFile))
                .thenReturn(imageResponse);

        when(imageMapper.toEntity(imageResponse))
                .thenReturn(image);

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        // Act
        var result = userService.signUp(signUpRequest, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse, result);
        verify(keycloakService, times(1)).signUp(signUpRequest);
        verify(userMapper, times(1)).toEntity(UUID.fromString(userRepresentation.getId()), signUpRequest);
        verify(imageService, times(1)).create(multipartFile);
        verify(imageMapper, times(1)).toEntity(imageResponse);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void resetVerificationEmail_withNonExistingUser_shouldThrowNotFoundException() {
        // Arrange
        var username = UserFactory.validUser().getUsername();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(UserNotFoundException.class, () -> userService.resetVerificationEmail(username));
        verify(userRepository, times(1)).findByUsername(username);
        verify(keycloakService, never()).resetVerificationEmail(any(UUID.class));
    }

    @Test
    void resetVerificationEmail_withExistingUser_shouldSucceed() {
        // Arrange
        var user = UserFactory.validUser();

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        doNothing()
                .when(keycloakService)
                .resetVerificationEmail(user.getKeycloakId());

        // Act && Assert
        assertDoesNotThrow(() -> userService.resetVerificationEmail(user.getUsername()));
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(keycloakService, times(1)).resetVerificationEmail(user.getKeycloakId());
    }

    @Test
    void changeEmail_withNonExistingUser_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var username = UserFactory.validUser().getUsername();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(UserNotFoundException.class, () -> userService.changeEmail(username));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(keycloakService, never()).changeEmail(any(UUID.class));
        }
    }

    @Test
    void changeEmail_withExistingUserAndUnauthorizedUser_shouldThrowForbiddenModificationException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenThrow(UserModificationForbiddenException.class);

            // Act && Assert
            assertThrows(UserModificationForbiddenException.class, () -> userService.changeEmail(username));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(keycloakService, never()).changeEmail(any(UUID.class));
        }
    }

    @Test
    void changeEmail_withExistingUserAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(keycloakService)
                    .changeEmail(keycloakId);

            // Act && Assert
            assertDoesNotThrow(() -> userService.changeEmail(username));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(keycloakService, times(1)).changeEmail(keycloakId);
        }
    }

    @Test
    void changeImage_withNonExistingUser_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var username = UserFactory.validUser().getUsername();
            var multipartFile = MultipartFileFactory.validFile();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(UserNotFoundException.class, () -> userService.changeImage(username, multipartFile));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(userRepository, times(1)).findByUsername(username);
            verify(imageService, never()).delete(any(Long.class));
            verify(imageService, never()).create(any(MultipartFile.class));
            verify(imageMapper, never()).toEntity(any(ImageResponse.class));
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void changeImage_withExistingUserAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            var user = UserFactory.validUserWithImage();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();
            var multipartFile = MultipartFileFactory.validFile();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenThrow(UserModificationForbiddenException.class);

            // Act && Assert
            assertThrows(UserModificationForbiddenException.class, () -> userService.changeImage(username, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, never()).delete(any(Long.class));
            verify(imageService, never()).create(any(MultipartFile.class));
            verify(imageMapper, never()).toEntity(any(ImageResponse.class));
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void changeImage_withExistingUserAndInvalidImage_shouldThrowImageNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();
            MultipartFile multipartFile = null;

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act && Assert
            assertThrows(ImageNotFoundException.class, () -> userService.changeImage(username, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, never()).delete(any(Long.class));
            verify(imageService, never()).create(any(MultipartFile.class));
            verify(imageMapper, never()).toEntity(any(ImageResponse.class));
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void changeImage_withExistingUserAndNullImageAndExistingUserImageAndAuthorizedUser_shouldRemoveImage() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUserWithImage();
            var username = user.getUsername();
            var userImageId = user.getImage().getId();
            var keycloakId = user.getKeycloakId();
            MultipartFile multipartFile = null;

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(imageService)
                    .delete(userImageId);

            when(userRepository.save(user))
                    .thenReturn(user);

            // Act && Assert
            assertDoesNotThrow(() -> userService.changeImage(username, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, times(1)).delete(userImageId);
            verify(imageService, never()).create(any());
            verify(imageMapper, never()).toEntity(any());
            verify(userRepository, times(1)).save(user);
        }
    }

    @Test
    void changeImage_withExistingUserAndNonExistingUserImageAndValidImageAndUserAuthorized_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();

            var multipartFile = MultipartFileFactory.validFile();
            var image = ImageFactory.validImage();
            var imageResponse = ImageResponseFactory.validResponse(image);

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            when(imageService.create(multipartFile))
                    .thenReturn(imageResponse);

            when(imageMapper.toEntity(imageResponse))
                    .thenReturn(image);

            when(userRepository.save(user))
                    .thenReturn(user);

            // Act && Assert
            assertDoesNotThrow(() -> userService.changeImage(username, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, never()).delete(any(Long.class));
            verify(imageService, times(1)).create(multipartFile);
            verify(imageMapper, times(1)).toEntity(imageResponse);
            verify(userRepository, times(1)).save(user);
        }
    }

    @Test
    void changeImage_withExistingUserAndExistingUserImageAndValidImageAndUserAuthorized_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUserWithImage();
            var username = user.getUsername();
            var userImageId = user.getImage().getId();
            var keycloakId = user.getKeycloakId();

            var multipartFile = MultipartFileFactory.validFile();
            var image = ImageFactory.validImage();
            var imageResponse = ImageResponseFactory.validResponse(image);

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(imageService)
                    .delete(userImageId);

            when(imageService.create(multipartFile))
                    .thenReturn(imageResponse);

            when(imageMapper.toEntity(imageResponse))
                    .thenReturn(image);

            when(userRepository.save(user))
                    .thenReturn(user);

            // Act && Assert
            assertDoesNotThrow(() -> userService.changeImage(username, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, times(1)).delete(userImageId);
            verify(imageService, times(1)).create(multipartFile);
            verify(imageMapper, times(1)).toEntity(imageResponse);
            verify(userRepository, times(1)).save(user);
        }
    }

    @Test
    void update_withNonExistingUser_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var updateRequest = UserUpdateRequestFactory.validRequest();
            var username = UserFactory.validUser().getUsername();
            var multipartFile = MultipartFileFactory.validFile();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(UserNotFoundException.class, () -> userService.update(username, updateRequest, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(imageService, never()).delete(any(Long.class));
            verify(imageService, never()).create(any(MultipartFile.class));
            verify(imageMapper, never()).toEntity(any(ImageResponse.class));
            verify(userMapper, never()).updateEntity(any(User.class), any(UserUpdateRequest.class));
            verify(userRepository, never()).save(any(User.class));
            verify(keycloakService, never()).update(any(UUID.class), ArgumentMatchers.any());
        }
    }

    @Test
    void update_withExistingUserAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var updateRequest = UserUpdateRequestFactory.validRequest();
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();
            var multipartFile = MultipartFileFactory.validFile();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenThrow(UserModificationForbiddenException.class);

            // Act && Assert
            assertThrows(UserModificationForbiddenException.class, () -> userService.update(username, updateRequest, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, never()).delete(any(Long.class));
            verify(imageService, never()).create(any(MultipartFile.class));
            verify(imageMapper, never()).toEntity(any(ImageResponse.class));
            verify(userMapper, never()).updateEntity(any(User.class), any(UserUpdateRequest.class));
            verify(userRepository, never()).save(any(User.class));
            verify(keycloakService, never()).update(any(UUID.class), ArgumentMatchers.any());
        }
    }

    @Test
    void update_withExistingUserAndNullUserImageAndNullImageAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var updateRequest = UserUpdateRequestFactory.validRequest();
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();
            MultipartFile multipartFile = null;

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(userMapper)
                    .updateEntity(user, updateRequest);

            when(userRepository.save(user))
                    .thenReturn(user);

            doNothing()
                    .when(keycloakService)
                    .update(keycloakId, updateRequest);

            // Act && Assert
            assertDoesNotThrow(() -> userService.update(username, updateRequest, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, never()).delete(any(Long.class));
            verify(imageService, never()).create(any(MultipartFile.class));
            verify(imageMapper, never()).toEntity(any(ImageResponse.class));
            verify(userMapper, times(1)).updateEntity(user, updateRequest);
            verify(userRepository, times(1)).save(user);
            verify(keycloakService, times(1)).update(keycloakId, updateRequest);
        }
    }

    @Test
    void update_withExistingUserAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var updateRequest = UserUpdateRequestFactory.validRequest();
            var user = UserFactory.validUserWithImage();
            var username = user.getUsername();
            var userImageId = user.getImage().getId();
            var keycloakId = user.getKeycloakId();

            var multipartFile = MultipartFileFactory.validFile();
            var image = ImageFactory.validImage();
            var imageResponse = ImageResponseFactory.validResponse(image);

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(imageService)
                    .delete(userImageId);

            when(imageService.create(multipartFile))
                    .thenReturn(imageResponse);

            when(imageMapper.toEntity(imageResponse))
                    .thenReturn(image);

            doNothing()
                    .when(userMapper)
                    .updateEntity(user, updateRequest);

            when(userRepository.save(user))
                    .thenReturn(user);

            doNothing()
                    .when(keycloakService)
                    .update(keycloakId, updateRequest);

            // Act && Assert
            assertDoesNotThrow(() -> userService.update(username, updateRequest, multipartFile));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(imageService, times(1)).delete(userImageId);
            verify(imageService, times(1)).create(multipartFile);
            verify(imageMapper, times(1)).toEntity(imageResponse);
            verify(userMapper, times(1)).updateEntity(user, updateRequest);
            verify(userRepository, times(1)).save(user);
            verify(keycloakService, times(1)).update(keycloakId, updateRequest);
        }
    }

    @Test
    void delete_withNonExistingUser_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var username = UserFactory.validUser().getUsername();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(UserNotFoundException.class, () -> userService.delete(username));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(userRepository, never()).delete(any(User.class));
            verify(keycloakService, never()).delete(any(UUID.class));
        }
    }

    @Test
    void delete_withExistingUserAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenThrow(UserModificationForbiddenException.class);

            // Act && Assert
            assertThrows(UserModificationForbiddenException.class, () -> userService.delete(username));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(userRepository, never()).delete(any(User.class));
            verify(keycloakService, never()).delete(any(UUID.class));
        }
    }

    @Test
    void delete_withExistingUserAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var user = UserFactory.validUser();
            var username = user.getUsername();
            var keycloakId = user.getKeycloakId();

            when(userRepository.findByUsername(username))
                    .thenReturn(Optional.of(user));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(userRepository)
                    .delete(user);

            doNothing()
                    .when(keycloakService)
                    .delete(keycloakId);

            // Act && Assert
            assertDoesNotThrow(() -> userService.delete(username));
            verify(userRepository, times(1)).findByUsername(username);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(keycloakId, UserModificationForbiddenException.class), times(1));
            verify(userRepository, times(1)).delete(user);
            verify(keycloakService, times(1)).delete(keycloakId);
        }
    }

}
