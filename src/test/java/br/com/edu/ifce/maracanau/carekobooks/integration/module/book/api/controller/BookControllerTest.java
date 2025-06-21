package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.common.layer.api.controller.form.MultipartFormDataRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri.BookUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.image.infrastructure.domain.entity.MultipartFileFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.*;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakAuthProvider;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.MinioProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.repository.ImageRepository;
import org.hibernate.Hibernate;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        MinioContainerConfig.class,
        PostgresContainerConfig.class,
        RedisContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private KeycloakAuthProvider keycloakAuthProvider;

    @Autowired
    private MinioProvider minioProvider;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() throws Exception {
        minioProvider.setUp();
        tearDown();
    }

    @AfterEach
    void tearDown() throws Exception {
        imageRepository.deleteAll();
        bookRepository.deleteAll();
        bookGenreRepository.deleteAll();
        keycloakAuthProvider.tearDown();
        minioProvider.tearDown();
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
    void search_withValidBookQuery_shouldReturnPagedSimplifiedBookResponse() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));

        // Act
        var uri = BookUriFactory.validUri();
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedBookResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(book.getId());
    }

    @ParameterizedTest
    @CsvSource({
            "id, false",
            "title, false",
            "authorName, false",
            "publisherName, false",
            "publishedAt, false",
            "pageCount, false",
            "createdAt, false",
            "updatedAt, false",
            "id, true",
            "title, true",
            "authorName, true",
            "publisherName, true",
            "publishedAt, true",
            "pageCount, true",
            "createdAt, true",
            "updatedAt, true"
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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookRepository.count()).isZero();
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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(book.getId());
    }

    @Test
    void create_withValidBookRequestAndValidImage_shouldReturnBookResponse() throws IOException {
        // Arrange
        var request = BookRequestFactory.validRequestWithEmptyGenres();
        var image = MultipartFileFactory.validFile();

        // Act
        var uri = BookUriFactory.validUri();
        var body = MultipartFormDataRequestFactory.validRequestWithImage(request, image);
        var httpEntity = new HttpEntity<>(body, keycloakAuthProvider.getMultipartFormDataAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, BookResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(1);
    }

    @Test
    void update_withExistingBookAndValidBookRequestAndValidImage_shouldReturnNoContent() throws IOException {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var request = BookRequestFactory.validRequest(List.of(genre));
        var image = MultipartFileFactory.validFile();

        // Act
        var uri = BookUriFactory.validUri(book.getId());
        var body = MultipartFormDataRequestFactory.validRequestWithImage(request, image);
        var httpEntity = new HttpEntity<>(body, keycloakAuthProvider.getMultipartFormDataAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Void.class);

        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        Hibernate.initialize(updatedBook.getGenres());

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(1);

        assertThat(updatedBook.getId()).isEqualTo(book.getId());
        assertThat(updatedBook.getTitle()).isEqualTo(request.getTitle());
        assertThat(updatedBook.getSynopsis()).isEqualTo(request.getSynopsis());
        assertThat(updatedBook.getAuthorName()).isEqualTo(request.getAuthorName());
        assertThat(updatedBook.getPublisherName()).isEqualTo(request.getPublisherName());
        assertThat(updatedBook.getPublishedAt()).isEqualTo(request.getPublishedAt());
        assertThat(updatedBook.getPageCount()).isEqualTo(request.getPageCount());
        assertThat(updatedBook.getGenres()).extracting(BookGenre::getName).containsExactlyElementsOf(request.getGenres());
        assertThat(updatedBook.getCreatedAt()).isEqualToIgnoringNanos(book.getCreatedAt());
    }

    @Test
    void assignGenre_withExistingBookAndExistingGenre_shouldReturnNoContent() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        var uri = BookUriFactory.validGenreUri(book.getId(), genre.getName());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Void.class);

        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        Hibernate.initialize(updatedBook.getGenres());

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(updatedBook.getGenres()).hasSize(1);
        assertThat(updatedBook.getGenres().getFirst().getName()).isEqualTo(genre.getName());
    }

    @Test
    void unassignGenre_withExistingBookAndExistingGenre_shouldReturnNoContent() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));

        // Act
        var uri = BookUriFactory.validGenreUri(book.getId(), genre.getName());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        Hibernate.initialize(updatedBook.getGenres());

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(updatedBook.getGenres()).isEmpty();
    }

    @Test
    void assignImage_withExistingBookAndValidImage_shouldReturnNoContent() throws IOException {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var image = MultipartFileFactory.validFile();

        // Act
        var uri = BookUriFactory.validImageUri(book.getId());
        var body = MultipartFormDataRequestFactory.validRequestWithImage(image);
        var httpEntity = new HttpEntity<>(body, keycloakAuthProvider.getMultipartFormDataAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(1);
    }

    @Test
    void unassingImage_withExistingBook_shouldReturnNoContent() {
        // Arrange
        var image = imageRepository.save(ImageFactory.validImageWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres(image));

        // Act
        var uri = BookUriFactory.validImageUri(book.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isZero();
    }

    @Test
    void delete_withExistingBook_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        var uri = BookUriFactory.validUri(book.getId());
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.count()).isZero();
    }

    @Test
    void clearCache_withExistingCache_shouldReturnNoContent() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        var getUri = BookUriFactory.validUri(book.getId());
        var getResponse = restTemplate.exchange(getUri, HttpMethod.GET, null, BookResponse.class);

        var deleteCacheUri = BookUriFactory.validCacheUri();
        var httpEntity = new HttpEntity<>(keycloakAuthProvider.getAuthorizationHeaders());
        var deleteCacheResponse = restTemplate.exchange(deleteCacheUri, HttpMethod.DELETE, httpEntity, Void.class);

        // Assert
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteCacheResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(Objects.requireNonNull(cacheManager.getCache("book")).get(book.getId())).isNull();
        assertThat(bookRepository.count()).isEqualTo(1);
    }

}
