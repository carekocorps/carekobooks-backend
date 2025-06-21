package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({PostgresContainerConfig.class})
@DataJpaTest
class BookProgressRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookProgressRepository bookProgressRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookProgressRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void changeAsFavoriteById_withValidProgressIdAndValidFavoriteStatus_shouldSucceed() {
        // Arrange
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var progress = BookProgressFactory.validProgressWithNullId(book, user);
        var progressIsFavorite = progress.getIsFavorite();
        var newProgressIsFavorite = !progressIsFavorite;
        bookProgressRepository.save(progress);

        // Act
        bookProgressRepository.changeAsFavoriteById(progress.getId(), newProgressIsFavorite);
        entityManager.clear();
        var updatedProgress = bookProgressRepository.findById(progress.getId()).orElseThrow();

        // Assert
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookProgressRepository.count()).isEqualTo(1);
        assertThat(updatedProgress.getIsFavorite()).isEqualTo(newProgressIsFavorite);
    }

}
