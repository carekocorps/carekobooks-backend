package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
@Import({PostgresContainerConfig.class})
@DataJpaTest
class BookGenreTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAllInBatch();
        bookGenreRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void preRemove_withExistingBooks_shouldSucceed() {
        // Arrange
        var genre = bookGenreRepository.save(BookGenreFactory.validGenreWithNullId());
        bookRepository.save(BookFactory.validBookWithNullId(List.of(genre)));

        entityManager.clear();
        var updatedGenre = bookGenreRepository.findById(genre.getId()).orElseThrow();

        // Act
        bookGenreRepository.delete(updatedGenre);

        // Assert
        assertThat(bookGenreRepository.count()).isZero();
        assertThat(bookRepository.count()).isEqualTo(1);
    }

}
