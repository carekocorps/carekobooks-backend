package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
@Import({PostgresContainerConfig.class})
@DataJpaTest
class BookTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookProgressRepository bookProgressRepository;

    @Autowired
    private BookReviewRepository bookReviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookReviewRepository.deleteAllInBatch();
        bookProgressRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void getUserAverageScore_withNonExistingProgresses_shouldReturnNullAverageScore() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        var result = book.getUserAverageScore();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(result).isNull();
    }

    @Test
    void getUserAverageScore_withExistingProgressAndNullScoreProgresses_shouldReturnNullAverageScore() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progresses = IntStream
                .range(0, 10)
                .mapToObj(x -> bookProgressRepository.save(BookProgressFactory.validProgressWithNullIdAndNullScore(book, user)))
                .toList();

        entityManager.clear();
        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();

        // Act
        var result = updatedBook.getUserAverageScore();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(progresses.size());
        assertThat(result).isNull();
    }

    @Test
    void getUserAverageScore_withExistingProgresses_shouldReturnAverageScore() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progresses = IntStream
                .range(0, 10)
                .mapToObj(x -> bookProgressRepository.save(BookProgressFactory.validProgressWithNullId(book, user)))
                .toList();

        var expectedAverageScore = progresses.stream()
                .filter(x -> x.getScore() != null)
                .mapToInt(BookProgress::getScore)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);

        entityManager.clear();
        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();

        // Act
        var result = updatedBook.getUserAverageScore();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(progresses.size());
        assertThat(result).isEqualTo(expectedAverageScore);
    }

    @Test
    void getReviewAverageScore_withNonExistingReviews_shouldReturnNullAverageScore() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        var result = book.getReviewAverageScore();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(result).isNull();
    }

    @Test
    void getReviewAverageScore_withExistingReviews_shouldReturnAverageScore() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var reviews = IntStream
                .range(0, 10)
                .mapToObj(x -> bookReviewRepository.save(BookReviewFactory.validReviewWithNullId(book, user)))
                .toList();

        var expectedAverageScore = reviews
                .stream()
                .filter(x -> x.getScore() != null)
                .mapToInt(BookReview::getScore)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);

        entityManager.clear();
        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();

        // Act
        var result = updatedBook.getReviewAverageScore();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookReviewRepository.count()).isEqualTo(reviews.size());
        assertThat(result).isEqualTo(expectedAverageScore);
    }

}
