package br.com.edu.ifce.maracanau.carekobooks.integration.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.common.layer.api.controller.form.MultipartFormDataRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.MultipartFileFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.api.controller.uri.UserUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.*;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.MinioProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.repository.ImageRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        MinioContainerConfig.class,
        PostgresContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private MinioProvider minioProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() throws Exception {
        minioProvider.setUp();
        tearDown();
    }

    @AfterEach
    void tearDown() throws Exception {
        imageRepository.deleteAll();
        userRepository.deleteAll();
        keycloakAuthProvider.tearDown();
        minioProvider.tearDown();
    }

    @Test
    void search_withValidUserQuery_shouldReturnPagedSimplifiedUserResponse() {
        // Arrange
        userRepository.save(UserFactory.validUserWithNullId());

        // Act
        var uri = UserUriFactory.validUri();
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedUserResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @ParameterizedTest
    @CsvSource({
            "id, false",
            "username, false",
            "createdAt, false",
            "updatedAt, false",
            "id, true",
            "username, true",
            "createdAt, true",
            "updatedAt, true"
    })
    void search_withValidUserQuery_shouldReturnPagedSimplifiedUserResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var user = userRepository.save(UserFactory.validUserWithNullId());

        // Act
        var uri = UserUriFactory.validQueryUri(user, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedUserResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void find_withNonExistingUser_shouldReturnNotFound() {
        // Arrange
        var username = UserFactory.validUser().getUsername();

        // Act
        var uri = UserUriFactory.validUri(username);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, UserResponse.class);

        // Assert
        assertThat(userRepository.count()).isZero();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_withExistingUser_shouldReturnUserResponse() {
        // Arrange
        var user = userRepository.save(UserFactory.validUserWithNullId());

        // Act
        var uri = UserUriFactory.validUri(user.getUsername());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, UserResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void signUp_withValidSignUpRequest_shouldReturnUserResponse() throws IOException {
        // Arrange
        var request = UserSignUpRequestFactory.validRequest();
        var image = MultipartFileFactory.validFile();

        // Act
        var uri = UserUriFactory.validUri();
        var body = MultipartFormDataRequestFactory.validRequestWithImage(request, image);
        var httpEntity = new HttpEntity<>(body, keycloakAuthProvider.getMultipartFormDataAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, UserResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(1);
    }

    @Test
    void resetVerificationEmail_withExistingUserAndNotVerifiedUser_shouldReturnNoContent() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = keycloakAuthProvider.create(signUpRequest, false);
        var user = userRepository.save(UserFactory.validUserWithNullId(UUID.fromString(userRepresentation.getId()), signUpRequest));

        // Act
        var uri = UserUriFactory.validResetVerificationEmailUri(user.getUsername());
        var response = restTemplate.exchange(uri, HttpMethod.POST, null, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void changeEmail_withExistingUserAndVerifiedUser_shouldReturnNoContent() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = keycloakAuthProvider.create(signUpRequest, true);
        var user = userRepository.save(UserFactory.validUserWithNullId(UUID.fromString(userRepresentation.getId()), signUpRequest));

        // Act
        var uri = UserUriFactory.validResetEmailUri(user.getUsername());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void update_withExistingUserAndValidUpdateRequest_shouldReturnNoContent() throws IOException {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = keycloakAuthProvider.create(signUpRequest, true);
        var user = userRepository.save(UserFactory.validUserWithNullId(UUID.fromString(userRepresentation.getId()), signUpRequest));

        var updateRequest = UserUpdateRequestFactory.validRequest();
        var image = MultipartFileFactory.validFile();

        // Act
        var uri = UserUriFactory.validUri(user.getUsername());
        var body = MultipartFormDataRequestFactory.validRequestWithImage(updateRequest, image);
        var httpEntity = new HttpEntity<>(body, keycloakAuthProvider.getMultipartFormDataAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Void.class);
        var updatedUser = userRepository.findById(user.getId()).orElseThrow();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(1);

        assertThat(updatedUser.getId()).isEqualTo(user.getId());
        assertThat(updatedUser.getKeycloakId()).isEqualTo(user.getKeycloakId());
        assertThat(updatedUser.getUsername()).isEqualTo(updateRequest.getUsername());
        assertThat(updatedUser.getDisplayName()).isEqualTo(updateRequest.getDisplayName());
        assertThat(updatedUser.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(updatedUser.getCreatedAt()).isEqualToIgnoringNanos(user.getCreatedAt());
    }

    @Test
    void assignImage_withExistingUserAndValidImage_shouldReturnNoContent() throws IOException {
        // Arrange
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var image = MultipartFileFactory.validFile();

        // Act
        var uri = UserUriFactory.validImagesUri(user.getUsername());
        var body = MultipartFormDataRequestFactory.validRequestWithImage(image);
        var httpEntity = new HttpEntity<>(body, keycloakAuthProvider.getMultipartFormDataAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(1);
    }

    @Test
    void unassignImage_withExistingUserAndValidUserImage_shouldReturnNoContent() {
        // Arrange
        var image = imageRepository.save(ImageFactory.validImageWithNullId());
        var user = userRepository.save(UserFactory.validUserWithNullIdAndImage(image));

        // Act
        var uri = UserUriFactory.validImagesUri(user.getUsername());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isZero();
    }

    @Test
    void delete_withExistingUser_shouldReturnNoContent() {
        // Arrange
        var signUpRequest = UserSignUpRequestFactory.validRequest();
        var userRepresentation = keycloakAuthProvider.create(signUpRequest, true);
        var user = userRepository.save(UserFactory.validUserWithNullId(UUID.fromString(userRepresentation.getId()), signUpRequest));

        // Act
        var uri = UserUriFactory.validUri(user.getUsername());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isZero();
        assertThat(imageRepository.count()).isZero();
    }

}
