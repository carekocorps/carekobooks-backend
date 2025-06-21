package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri.BookThreadReplyUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookThreadReplyRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.DynamicPropertyRegistrarConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadReplyRepository;
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

@Import({
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookThreadReplyControllerTest {

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

    @Autowired
    private BookThreadReplyRepository bookThreadReplyRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookThreadReplyRepository.deleteAll();
        bookThreadRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        keycloakAuthProvider.tearDown();
    }

    @Test
    void search_withValidReplyQuery_shouldReturnPagedReplyResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));

        // Act
        var uri = BookThreadReplyUriFactory.validUri();
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookThreadReplyResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @ParameterizedTest
    @CsvSource({
            "id, false",
            "createdAt, false",
            "updatedAt, false",
            "id, true",
            "createdAt, true",
            "createdAt, true"
    })
    void search_withValidReplyQuery_shouldReturnPagedReplyResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var parentReply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));
        var childReply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(parentReply, thread, user));

        // Act
        var uri = BookThreadReplyUriFactory.validQueryUri(childReply, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookThreadReplyResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(2);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(childReply.getId());
    }

    @Test
    void find_withNonExistingReply_shouldReturnNotFound() {
        // Arrange
        var replyId = Math.abs(new Random().nextLong()) + 1;

        // Act
        var uri = BookThreadReplyUriFactory.validUri(replyId);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadReplyResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookThreadReplyRepository.count()).isZero();
    }

    @Test
    void find_withExistingReply_shouldReturnReplyResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var reply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));

        // Act
        var uri = BookThreadReplyUriFactory.validUri(reply.getId());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadReplyResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reply.getId());
    }

    @Test
    void create_withValidReplyRequest_shouldReturnReplyResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var request = BookThreadReplyRequestFactory.validRequest(thread, user);

        // Act
        var uri = BookThreadReplyUriFactory.validUri();
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, BookThreadReplyResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(1);
    }

    @Test
    void addChild_withExistingReplyAndValidReplyRequest_shouldReturnReplyResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var parentReply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));
        var childReplyRequest = BookThreadReplyRequestFactory.validRequest(thread, user);

        // Act
        var uri = BookThreadReplyUriFactory.validChildrenUri(parentReply.getId());
        var httpEntity = new HttpEntity<>(childReplyRequest, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, BookThreadReplyResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(2);
    }

    @Test
    void update_withExistingReplyAndValidReplyRequest_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var reply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));
        var request = BookThreadReplyRequestFactory.validRequest(thread, user);

        // Act
        var uri = BookThreadReplyUriFactory.validUri(reply.getId());
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Void.class);
        var updatedReply = bookThreadReplyRepository.findById(reply.getId()).orElseThrow();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(1);

        assertThat(updatedReply.getId()).isEqualTo(reply.getId());
        assertThat(updatedReply.getContent()).isEqualTo(request.getContent());
        assertThat(updatedReply.getUser().getUsername()).isEqualTo(request.getUsername());
        assertThat(updatedReply.getThread().getId()).isEqualTo(request.getThreadId());
        assertThat(updatedReply.getCreatedAt()).isEqualToIgnoringNanos(reply.getCreatedAt());
    }

    @Test
    void delete_withExistingReply_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var reply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));

        // Act
        var uri = BookThreadReplyUriFactory.validUri(reply.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isZero();
    }

}
