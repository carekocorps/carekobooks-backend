package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    private Book savedBook;

    @Test
    @Transactional
    void updateUserAverageScoreById_ReturnsSuccess() {
        Double newScore = 4.5;
        Long bookId = savedBook.getId();

        bookRepository.updateUserAverageScoreById(newScore, bookId);

        entityManager.flush();
        entityManager.clear();

        Optional<Book> updatedBook = bookRepository.findById(bookId);
        assertTrue(updatedBook.isPresent());
        assertEquals(newScore, updatedBook.get().getUserAverageScore());
    }

    @Test
    @Transactional
    void updateUserAverageScoreById_ReturnsFailure() {
        Double newScore = 4.5;
        Long bookId = 999999999L;

        bookRepository.updateUserAverageScoreById(newScore, bookId);

        entityManager.flush();
        entityManager.clear();

        Optional<Book> book = bookRepository.findById(savedBook.getId());
        assertTrue(book.isPresent());
        assertEquals(3.5, book.get().getUserAverageScore());
    }

    @Test
    @Transactional
    void updateReviewAverageScoreById_ReturnsSuccess() {
        Double newScore = 4.5;
        Long bookId = savedBook.getId();

        bookRepository.updateReviewAverageScoreById(newScore, bookId);

        entityManager.flush();
        entityManager.clear();

        Optional<Book> updatedBook = bookRepository.findById(bookId);
        assertTrue(updatedBook.isPresent());
        assertEquals(newScore, updatedBook.get().getReviewAverageScore());
    }

    @Test
    @Transactional
    void updateReviewAverageScoreById_ReturnsFailure() {
        Double newScore = 4.5;
        Long bookId = 999999L;

        bookRepository.updateReviewAverageScoreById(newScore, bookId);

        entityManager.flush();
        entityManager.clear();

        Optional<Book> book = bookRepository.findById(savedBook.getId());
        assertTrue(book.isPresent());
        assertEquals(4.0, book.get().getReviewAverageScore());
    }

    @BeforeEach
    void setUp() {
        var book = new Book();
        book.setTitle("Diário de Larissa Manoela");
        book.setSynopsis("muito legal");
        book.setAuthor("Otávio Alcantara");
        book.setPublisher("Editora NayetDet");
        book.setPublishedAt(LocalDate.of(2020, 1, 1));
        book.setTotalPages(200);
        book.setUserAverageScore(3.5);
        book.setReviewAverageScore(4.0);

        savedBook = bookRepository.save(book);

        entityManager.flush();
        entityManager.clear();
    }
}