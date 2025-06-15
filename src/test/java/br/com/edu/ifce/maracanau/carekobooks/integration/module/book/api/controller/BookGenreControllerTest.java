package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookGenreQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerTestConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.RedisContainerTestConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.SecurityTestConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@Import({PostgresContainerTestConfig.class, RedisContainerTestConfig.class, SecurityTestConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookGenreControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @BeforeEach
    void setUp() {
        bookGenreRepository.deleteAllInBatch();
    }

    @AfterEach
    void tearDown() {
        setUp();
    }

    @ParameterizedTest
    @CsvSource({
        "id,false",
        "displayName,false",
        "createAt,false",
        "updatedAt,false",
        "id,true",
        "displayName,true",
        "createAt,true",
        "updatedAt,true"
    })
    void search_withValidGenreQuery_shouldReturnPagedGenreResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var uri = BookGenreQueryFactory.validURIString(genre, orderBy, isAscendingOrder);

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookGenreResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(genre.getId());
    }

    @Test
    void find_withNonExistingGenre_shouldReturnNotFound() {
        // Arrange
        var genreName = BookGenreFactory.validGenre().getName();
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/genres")
                .pathSegment(genreName)
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookGenreResponse.class);

        // Assert
        assertThat(bookGenreRepository.count()).isZero();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_withExistingGenre_shouldReturnGenreResponse() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var uri = UriComponentsBuilder
                .fromPath("/api/v1/books/genres")
                .pathSegment(genre.getName())
                .build()
                .toUriString();

        // Act
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookGenreResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(genre.getId());
    }

}
