package br.com.edu.ifce.maracanau.carekobooks.integration.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.api.controller.uri.UserUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.DynamicPropertyRegistrarConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
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

import static org.assertj.core.api.Assertions.assertThat;

@Import({
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserSocialControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        keycloakAuthProvider.tearDown();
    }

    @Test
    void search_withValidUserSocialFollowingQuery_shouldReturnPagedSimplifiedUserResponse() {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullIdAndFollowing(userFollowed));

        // Act
        var uri = UserUriFactory.validSocialFollowingQueryUri(userFollowing);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedUserResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(userFollowed.getId());
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
    void search_withValidUserSocialFollowingQuery_shouldReturnPagedSimplifiedUserResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullIdAndFollowing(userFollowed));

        // Act
        var uri = UserUriFactory.validSocialFollowingQueryUri(userFollowing, userFollowed, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedUserResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(userFollowed.getId());
    }

    @Test
    void search_withValidUserSocialFollowersQuery_shouldReturnPagedSimplifiedUserResponse() {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullIdAndFollowing(userFollowed));

        // Act
        var uri = UserUriFactory.validSocialFollowersQueryUri(userFollowed);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedUserResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(userFollowing.getId());
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
    void search_withValidUserSocialFollowersQuery_shouldReturnPagedSimplifiedUserResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullIdAndFollowing(userFollowed));

        // Act
        var uri = UserUriFactory.validSocialFollowersQueryUri(userFollowing, userFollowed, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedUserResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(userFollowing.getId());
    }

    @Test
    void follow_withExistingUserFollowingAndExistingUserFollowed_shouldReturnNoContent() {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullId());

        // Act
        var uri = UserUriFactory.validFollowingUri(userFollowing.getUsername(), userFollowed.getUsername());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isEqualTo(2);
    }

    @Test
    void unfollow_withExistingUserFollowingAndExistingUserFollowed_shouldReturnNoContent() {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullIdAndFollowing(userFollowed));

        // Act
        var uri = UserUriFactory.validFollowingUri(userFollowing.getUsername(), userFollowed.getUsername());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.count()).isEqualTo(2);
    }

}
