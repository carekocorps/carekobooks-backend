package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookThreadQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerTestConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.SecurityTestConfig;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Import({PostgresContainerTestConfig.class, SecurityTestConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookThreadControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookThreadRepository bookThreadRepository;

    @BeforeEach
    void setUp() {
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
            "title,false",
            "createdAt,false",
            "updatedAt,false",
            "id,true",
            "title,true",
            "createdAt,true",
            "updatedAt,true"
    })
    void search_withValidThreadQuery_shouldReturnPagedThreadResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var uri = BookThreadQueryFactory.validURIString(thread, orderBy, isAscendingOrder);

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookThreadResponse>>() {});
        var result = response.getBody();

        // Assert
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
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/threads")
                .pathSegment(String.valueOf(threadId))
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadResponse.class);

        // Assert
        assertThat(bookThreadRepository.count()).isZero();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_withExistingThread_shouldReturnThreadResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/threads")
                .pathSegment(String.valueOf(thread.getId()))
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookThreadResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(thread.getId());
    }

}
