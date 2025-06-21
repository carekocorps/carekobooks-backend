package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.api.controller.uri.BookReviewUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookReviewRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.DynamicPropertyRegistrarConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
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
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookReviewControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookReviewRepository bookReviewRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookReviewRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        keycloakAuthProvider.tearDown();
    }

    @Test
    void search_withValidReviewQuery_shouldReturnPagedReviewResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var review = bookReviewRepository.save(BookReviewFactory.validReviewWithNullId(book, user));

        // Act
        var uri = BookReviewUriFactory.validUri();
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedBookResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookReviewRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(review.getId());
    }

    @ParameterizedTest
    @CsvSource({
        "id, false",
        "username, false",
        "score, false",
        "createdAt, false",
        "updatedAt, false",
        "id, true",
        "username, true",
        "score, true",
        "createdAt, true",
        "updatedAt, true"
    })
    void search_withValidReviewQuery_shouldReturnPagedReviewResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var review = bookReviewRepository.save(BookReviewFactory.validReviewWithNullId(book, user));

        // Act
        var uri = BookReviewUriFactory.validQueryUri(review, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedBookResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookReviewRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(review.getId());
    }

    @Test
    void find_withNonExistingReview_shouldReturnNotFound() {
        // Arrange
        var reviewId = Math.abs(new Random().nextLong()) + 1;

        // Act
        var uri = BookReviewUriFactory.validUri(reviewId);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookReviewResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookReviewRepository.count()).isZero();
    }

    @Test
    void find_withExistingReview_shouldReturnReviewResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var review = bookReviewRepository.save(BookReviewFactory.validReviewWithNullId(book, user));

        // Act
        var uri = BookReviewUriFactory.validUri(review.getId());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookReviewResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookReviewRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(review.getId());
    }

    @Test
    void create_withValidReviewRequest_shouldReturnReviewResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var request = BookReviewRequestFactory.validRequest(book, user);

        // Act
        var uri = BookReviewUriFactory.validUri();
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, BookReviewResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookReviewRepository.count()).isEqualTo(1);
    }

    @Test
    void update_withExistingReviewAndValidReviewRequest_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var review = bookReviewRepository.save(BookReviewFactory.validReviewWithNullId(book, user));
        var request = BookReviewRequestFactory.validRequest(book, user);

        // Act
        var uri = BookReviewUriFactory.validUri(review.getId());
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Void.class);
        var updatedReview = bookReviewRepository.findById(review.getId()).orElseThrow();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookReviewRepository.count()).isEqualTo(1);

        assertThat(updatedReview.getId()).isEqualTo(review.getId());
        assertThat(updatedReview.getTitle()).isEqualTo(request.getTitle());
        assertThat(updatedReview.getContent()).isEqualTo(request.getContent());
        assertThat(updatedReview.getScore()).isEqualTo(request.getScore());
        assertThat(updatedReview.getBook().getId()).isEqualTo(request.getBookId());
        assertThat(updatedReview.getUser().getUsername()).isEqualTo(request.getUsername());
        assertThat(updatedReview.getCreatedAt()).isEqualToIgnoringNanos(updatedReview.getCreatedAt());
    }

    @Test
    void delete_withExistingReview_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var review = bookReviewRepository.save(BookReviewFactory.validReviewWithNullId(book, user));

        // Act
        var uri = BookReviewUriFactory.validUri(review.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookReviewRepository.count()).isZero();
    }

}
