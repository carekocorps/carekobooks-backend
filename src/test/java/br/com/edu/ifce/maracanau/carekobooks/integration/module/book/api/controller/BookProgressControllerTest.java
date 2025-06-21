package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.api.controller.uri.BookProgressUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.DynamicPropertyRegistrarConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
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

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookProgressControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookProgressRepository bookProgressRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookProgressRepository.deleteAll();
        bookRepository.deleteAll();
        bookGenreRepository.deleteAll();
        userRepository.deleteAll();
        keycloakAuthProvider.tearDown();
    }

    @Test
    void search_withValidProgressQuery_shouldReturnPagedProgressResponse() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));

        // Act
        var uri = BookProgressUriFactory.validUri();
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookProgressResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(progress.getId());
    }

    @ParameterizedTest
    @CsvSource({
            "id, false",
            "username, false",
            "status, false",
            "isFavorite, false",
            "score, false",
            "pageCount, false",
            "createdAt, false",
            "updatedAt, false",
            "id, true",
            "username, true",
            "status, true",
            "isFavorite, true",
            "score, true",
            "pageCount, true",
            "createdAt, true",
            "updatedAt, true"
    })
    void search_withValidProgressQuery_shouldReturnPagedProgressResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));

        // Act
        var uri = BookProgressUriFactory.validQueryUri(progress, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookProgressResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(progress.getId());
    }

    @Test
    void find_withNonExistingProgress_shouldReturnNotFound() {
        // Arrange
        var progressId = Math.abs(new Random().nextLong()) + 1;

        // Act
        var uri = BookProgressUriFactory.validUri(progressId);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookProgressResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookProgressRepository.count()).isZero();
    }

    @Test
    void find_withExistingProgress_shouldReturnProgressResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));

        // Act
        var uri = BookProgressUriFactory.validUri(progress.getId());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookProgressResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(progress.getId());
    }

    @Test
    void create_withValidProgressRequest_shouldReturnProgressResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var request = BookProgressRequestFactory.validRequest(book, user);

        // Act
        var uri = BookProgressUriFactory.validUri();
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, BookProgressResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders()).containsKey("X-BookActivity-Created");
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);
    }

    @Test
    void update_withExistingProgressAndValidProgressRequest_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));
        var request = BookProgressRequestFactory.validRequest(book, user);

        // Act
        var uri = BookProgressUriFactory.validUri(progress.getId());
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, BookProgressResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getHeaders()).containsKey("X-BookActivity-Created");
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);
    }

    @Test
    void assignAsFavorite_withExistingProgress_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));

        // Act
        var uri = BookProgressUriFactory.validFavoritesUri(progress.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Void.class);
        var updatedProgress = bookProgressRepository.findById(progress.getId()).orElseThrow();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);
        assertThat(updatedProgress.getIsFavorite()).isTrue();
    }

    @Test
    void unassignAsFavorite_withExistingProgress_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));

        // Act
        var uri = BookProgressUriFactory.validFavoritesUri(progress.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);
        var updatedProgress = bookProgressRepository.findById(progress.getId()).orElseThrow();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);
        assertThat(updatedProgress.getIsFavorite()).isFalse();
    }

    @Test
    void delete_withExistingProgress_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));

        // Act
        var uri = BookProgressUriFactory.validUri(progress.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isZero();
    }

}
