package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.api.controller.uri.BookGenreUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookGenreRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.RedisContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
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
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class,
        RedisContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookGenreControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookGenreRepository.deleteAll();
        keycloakAuthProvider.tearDown();
        cacheManager
                .getCacheNames()
                .forEach(cacheName -> {
                    var cache = cacheManager.getCache(cacheName);
                    if (cache != null) {
                        cache.clear();
                    }
                });
    }

    @Test
    void search_withValidGenreQuery_shouldReturnPagedGenreResponse() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());

        // Act
        var uri = BookGenreUriFactory.validUri();
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookGenreResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(genre.getId());
    }

    @ParameterizedTest
    @CsvSource({
        "id, false",
        "displayName, false",
        "createAt, false",
        "updatedAt, false",
        "id, true",
        "displayName, true",
        "createAt, true",
        "updatedAt, true"
    })
    void search_withValidGenreQuery_shouldReturnPagedGenreResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());

        // Act
        var uri = BookGenreUriFactory.validQueryUri(genre, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookGenreResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(genre.getId());
    }

    @Test
    void find_withNonExistingGenre_shouldReturnNotFound() {
        // Arrange
        var genreName = BookGenreFactory.validGenre().getName();

        // Act
        var uri = BookGenreUriFactory.validUri(genreName);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookGenreResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookGenreRepository.count()).isZero();
    }

    @Test
    void find_withExistingGenre_shouldReturnGenreResponse() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());

        // Act
        var uri = BookGenreUriFactory.validUri(genre.getName());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, BookGenreResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(genre.getId());
    }

    @Test
    void create_withValidGenreRequest_shouldReturnGenre() {
        // Arrange
        var request = BookGenreRequestFactory.validRequest();

        // Act
        var uri = BookGenreUriFactory.validUri();
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri,  HttpMethod.POST, httpEntity, BookGenreResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
    }

    @Test
    void update_withExistingGenreAndValidGenreRequest_shouldReturnNoContent() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var request = BookGenreRequestFactory.validRequest();

        // Act
        var uri = BookGenreUriFactory.validUri(genre.getName());
        var httpEntity = new HttpEntity<>(request, keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Void.class);
        var updatedGenre = bookGenreRepository.findByName(request.getName()).orElseThrow();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(updatedGenre.getId()).isEqualTo(genre.getId());
        assertThat(updatedGenre.getName()).isEqualTo(request.getName());
        assertThat(updatedGenre.getDisplayName()).isEqualTo(request.getDisplayName());
        assertThat(updatedGenre.getDescription()).isEqualTo(request.getDescription());
        assertThat(updatedGenre.getCreatedAt()).isEqualToIgnoringNanos(genre.getCreatedAt());
    }

    @Test
    void delete_withExistingGenre_shouldReturnNoContent() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());

        // Act
        var uri = BookGenreUriFactory.validUri(genre.getName());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, BookGenreResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookGenreRepository.count()).isZero();
    }

    @Test
    void clearCache_withExistingCache_shouldReturnNoContent() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());

        // Act
        var getUri = BookGenreUriFactory.validUri(genre.getName());
        var getResponse = restTemplate.exchange(getUri, HttpMethod.GET, null, BookGenreResponse.class);

        var deleteCacheUri = BookGenreUriFactory.validCacheUri();
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var deleteCacheResponse = restTemplate.exchange(deleteCacheUri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteCacheResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(Objects.requireNonNull(cacheManager.getCache("book:genre")).get(genre.getName())).isNull();
        assertThat(bookGenreRepository.count()).isEqualTo(1);
    }

}
