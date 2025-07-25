package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.api.controller.uri.BookThreadUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookThreadRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
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

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookThreadControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookThreadRepository bookThreadRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookThreadRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        keycloakAuthProvider.tearDown();
    }

    @Test
    void search_withValidThreadQuery_shouldReturnPagedThreadResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));

        // Act
        var uri = BookThreadUriFactory.validUri();
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookThreadResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(thread.getId());
    }

    @ParameterizedTest
    @CsvSource({
            "id, false",
            "title, false",
            "createdAt, false",
            "updatedAt, false",
            "id, true",
            "title, true",
            "createdAt, true",
            "updatedAt, true"
    })
    void search_withValidThreadQuery_shouldReturnPagedThreadResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));

        // Act
        var uri = BookThreadUriFactory.validQueryUri(thread, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookThreadResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(thread.getId());
    }

    @Test
    void find_withNonExistingThread_shouldReturnNotFound() {
        // Arrange
        var threadId = Math.abs(new Random().nextLong()) + 1;

        // Act
        var uri = BookThreadUriFactory.validUri(threadId);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookThreadRepository.count()).isZero();
    }

    @Test
    void find_withExistingThread_shouldReturnThreadResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));

        // Act
        var uri = BookThreadUriFactory.validUri(thread.getId());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(thread.getId());
    }

    @Test
    void create_withValidThreadRequest_shouldReturnThreadResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var request = BookThreadRequestFactory.validRequest(book, user);

        // Act
        var uri = BookThreadUriFactory.validUri();
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, BookThreadResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
    }

    @Test
    void update_withExistingThreadAndValidThreadRequest_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var request = BookThreadRequestFactory.validRequest(book, user);

        // Act
        var uri = BookThreadUriFactory.validUri(thread.getId());
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Void.class);
        var updatedThread = bookThreadRepository.findById(thread.getId()).orElseThrow();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);

        assertThat(updatedThread.getId()).isEqualTo(thread.getId());
        assertThat(updatedThread.getTitle()).isEqualTo(request.getTitle());
        assertThat(updatedThread.getDescription()).isEqualTo(request.getDescription());
        assertThat(updatedThread.getBook().getId()).isEqualTo(request.getBookId());
        assertThat(updatedThread.getUser().getUsername()).isEqualTo(request.getUsername());
        assertThat(updatedThread.getCreatedAt()).isEqualToIgnoringNanos(thread.getCreatedAt());
    }

    @Test
    void delete_withExistingThread_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));

        // Act
        var uri = BookThreadUriFactory.validUri(thread.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isZero();
    }

}
