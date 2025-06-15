package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.TestContainersConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.TestSecurityConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestContainersConfig.class, TestSecurityConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAllInBatch();
        bookGenreRepository.deleteAllInBatch();
    }

    @AfterEach
    void tearDown() {
        setUp();
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
    void search_withValidBookQuery_shouldReturnPagedBookResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));

        // Act
        var uri = BookQueryFactory.validURI(book, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<SimplifiedBookResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(book.getId());
    }

}
