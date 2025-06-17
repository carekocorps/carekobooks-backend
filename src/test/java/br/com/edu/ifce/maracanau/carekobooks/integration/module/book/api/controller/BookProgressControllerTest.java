package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookProgressQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresTestcontainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakTestcontainerConfig;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Import({PostgresTestcontainerConfig.class, KeycloakTestcontainerConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookProgressControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
        bookProgressRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        bookGenreRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @AfterEach
    void tearDown() {
        setUp();
    }

    @ParameterizedTest
    @CsvSource({
            "id,false",
            "username,false",
            "status,false",
            "isFavorite,false",
            "score,false",
            "pageCount,false",
            "createdAt,false",
            "updatedAt,false",
            "id,true",
            "username,true",
            "status,true",
            "isFavorite,true",
            "score,true",
            "pageCount,true",
            "createdAt,true",
            "updatedAt,true"
    })
    void search_withValidProgressQuery_shouldReturnPagedProgressResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));
        var uri = BookProgressQueryFactory.validURIString(progress, orderBy, isAscendingOrder);

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookProgressResponse>>() {});
        var result = response.getBody();

        // Assert
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
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/progresses")
                .pathSegment(String.valueOf(progressId))
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookProgressResponse.class);

        // Assert
        assertThat(bookProgressRepository.count()).isZero();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_withExistingProgress_shouldReturnProgressResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user));
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/progresses")
                .pathSegment(String.valueOf(progress.getId()))
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookProgressResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(progress.getId());
    }

}
