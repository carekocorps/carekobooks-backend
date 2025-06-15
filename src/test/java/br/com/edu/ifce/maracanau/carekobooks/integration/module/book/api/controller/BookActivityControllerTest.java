package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookActivityFollowingQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query.BookActivityQueryFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.TestContainersConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.TestSecurityConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestContainersConfig.class, TestSecurityConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookActivityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookActivityRepository bookActivityRepository;

    @BeforeEach
    void setUp() {
        bookActivityRepository.deleteAllInBatch();
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
            "pageCount,false",
            "createdAt,false",
            "updatedAt,false",
            "id,true",
            "username,true",
            "status,true",
            "pageCount,true",
            "createdAt,true",
            "updatedAt,true"
    })
    void search_withValidActivityQuery_shouldReturnPagedActivityResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var activity = bookActivityRepository.save(BookActivityFactory.validActivityWithNullId(book, user));

        // Act
        var uri = BookActivityQueryFactory.validURI(activity, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookActivityResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookActivityRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(activity.getId());
    }

    @ParameterizedTest
    @CsvSource({
            "id,false",
            "createdAt,false",
            "updatedAt,false",
            "id,true",
            "createdAt,true",
            "updatedAt,true"
    })
    void search_withValidActivityFollowingQuery_shouldReturnPagedActivityResponse(String orderBy, boolean isAscendingOrder) {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullIdAndFollowing(userFollowed));
        var activity = bookActivityRepository.save(BookActivityFactory.validActivityWithNullId(book, userFollowed));

        // Act
        var uri = BookActivityFollowingQueryFactory.validURI(userFollowing.getUsername(), activity, orderBy, isAscendingOrder);
        var response = restTemplate.exchange(uri.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<ApplicationPage<BookActivityResponse>>() {});
        var result = response.getBody();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(bookActivityRepository.count()).isEqualTo(1);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(activity.getId());
    }

}
