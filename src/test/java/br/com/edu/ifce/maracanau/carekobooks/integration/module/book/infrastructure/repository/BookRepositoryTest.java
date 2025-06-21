package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({PostgresContainerConfig.class})
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach()
    void tearDown() {
        bookRepository.deleteAllInBatch();
        bookGenreRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void addGenre_withValidBookIdAndValidGenreId_shouldSucceed() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());

        // Act
        bookRepository.addGenre(book.getId(), genre.getId());
        entityManager.clear();
        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();

        // Assert
        assertThat(bookGenreRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(updatedBook.getGenres()).hasSize(1);
        assertThat(updatedBook.getGenres()).extracting(BookGenre::getName).containsExactly(genre.getName());
    }

    @Test
    void removeGenre_withValidBookIdAndValidGenreId_shouldSucceed() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));

        // Act
        bookRepository.removeGenre(book.getId(), genre.getId());
        entityManager.clear();
        var updatedBook = bookRepository.findById(book.getId()).orElseThrow();

        // Assert
        assertThat(bookGenreRepository.count()).isEqualTo(1);

        assertThat(updatedBook.getGenres()).isEmpty();
    }

}
