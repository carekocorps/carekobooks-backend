package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookReviewRepositoryTest {

    @Autowired
    private BookReviewRepository repository;

    @Autowired
    private EntityManager entityManager;

    private Book book;
    private User user;

    @Test
    @Transactional
    void findReviewAverageScoreByBookId_ReturnsSuccess() {

        createBookReview(book, user, 2);
        createBookReview(book, user, 5);

        var average = repository.findReviewAverageScoreByBookId(book.getId());

        assertNotNull(average);
        assertEquals(3.666, average, 0.001);
    }

    @Test
    @Transactional
    void findReviewAverageScoreByBookId_ReturnsFailure() {
        Double averageScore = repository.findReviewAverageScoreByBookId(999L);
        assertNull(averageScore);
    }

    @BeforeEach
    void setUp() {
        book = createBook();
        user = createUser();
        var review = new BookReview();
        review.setTitle("Review Inicial");
        review.setContent("Conteúdo da review");
        review.setScore(4);
        review.setBook(book);
        review.setUser(user);

        repository.save(review);

        entityManager.flush();
        entityManager.clear();
    }

    private void createBookReview(Book book, User user, int score) {
        var review = new BookReview();
        review.setTitle("Review " + score);
        review.setContent("Conteúdo da review " + score);
        review.setScore(score);
        review.setBook(book);
        review.setUser(user);
        entityManager.persist(review);
    }

    private Book createBook() {
        var book = new Book();
        book.setTitle("Diário de Larissa Manoela");
        book.setSynopsis("muito legal");
        book.setAuthor("Otávio Alcantara");
        book.setPublisher("Editora NayetDet");
        book.setPublishedAt(LocalDate.of(2020, 1, 1));
        book.setTotalPages(200);
        book.setUserAverageScore(3.5);
        book.setReviewAverageScore(4.0);
        entityManager.persist(book);
        return book;
    }

    private User createUser() {
        var user = new User();
        user.setUsername("larissa.manoela");
        user.setName("Larissa Manoela");
        user.setEmail("larissa@example.com");
        user.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMy.Mr/.VHwHqLHztx7XlR.H1d9Z5vHYqO2");
        user.setDescription("Atriz e cantora brasileira");
        user.setRole(UserRole.USER);
        entityManager.persist(user);
        return user;
    }
}