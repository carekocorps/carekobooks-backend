package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri.BookUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.DynamicPropertyRegistrarConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.RedisContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
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

@Import({
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class,
        RedisContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAllInBatch();
        bookGenreRepository.deleteAllInBatch();
        keycloakAuthProvider.tearDown();
    }

    @ParameterizedTest
    @CsvSource({
            "id,false",
            "title,false",
            "authorName,false",
            "publisherName,false",
            "publishedAt,false",
            "pageCount,false",
            "createdAt,false",
            "updatedAt,false",
            "id,true",
            "title,true",
            "authorName,true",
            "publisherName,true",
            "publishedAt,true",
            "pageCount,true",
            "createdAt,true",
            "updatedAt,true"
    })
    void search_withValidBookQuery_shouldReturnPagedSimplifiedBookResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));

        // Act
        var uri = BookUriFactory.validQueryUri(book, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedBookResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(book.getId());
    }

    @Test
    void find_withNonExistingBook_shouldReturnNotFound() {
        // Arrange
        var bookId = Math.abs(new Random().nextLong()) + 1;

        // Act
        var uri = BookUriFactory.validUri(bookId);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookResponse.class);

        // Assert
        assertThat(bookRepository.count()).isZero();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_withExistingBook_shouldReturnBookResponse() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        var uri = BookUriFactory.validUri(book.getId());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(book.getId());
    }

    @Test
    void delete_withExistingBook_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        var uri = BookUriFactory.validUri(book.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeader());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(bookRepository.count()).isZero();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
