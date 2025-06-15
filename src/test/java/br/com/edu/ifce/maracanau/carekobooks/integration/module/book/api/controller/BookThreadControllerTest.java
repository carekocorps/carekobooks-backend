package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookThreadQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.TestContainersConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.TestSecurityConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestContainersConfig.class, TestSecurityConfig.class})
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

        // Act
        var uri = BookThreadQueryFactory.validURI(thread, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookThreadResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(thread.getId());
    }

}
