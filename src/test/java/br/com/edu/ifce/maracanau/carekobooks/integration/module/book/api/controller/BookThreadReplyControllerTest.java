package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookThreadReplyQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerTestConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.SecurityTestConfig;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Import({PostgresContainerTestConfig.class, SecurityTestConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookThreadReplyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
        bookThreadReplyRepository.deleteAllInBatch();
        bookThreadRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @AfterEach
    void tearDown() {
        setUp();
    }

    @ParameterizedTest
    @CsvSource({
            "id,false",
            "createdAt,false",
            "updatedAt,false",
            "id,true",
            "createdAt,true",
            "createdAt,true"
    })
    void search_withValidReplyQuery_shouldReturnPagedReplyResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var parentReply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));
        var childReply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(parentReply, thread, user));
        var uri = BookThreadReplyQueryFactory.validURIString(childReply, orderBy, isAscendingOrder);

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookThreadReplyResponse>>() {});
        var result = response.getBody();

        // Assert
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
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/threads/replies")
                .pathSegment(String.valueOf(replyId))
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadReplyResponse.class);

        // Assert
        assertThat(bookThreadReplyRepository.count()).isZero();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_withExistingReply_shouldReturnReplyResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var reply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/threads/replies")
                .pathSegment(String.valueOf(reply.getId()))
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadReplyResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reply.getId());
    }

}
