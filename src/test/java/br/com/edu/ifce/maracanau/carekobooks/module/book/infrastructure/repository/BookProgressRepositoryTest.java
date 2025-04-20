package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole;
import jakarta.persistence.EntityManager;
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
class BookProgressRepositoryTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private BookProgressRepository repository;

    private Book book;
    private User user;

    @Test
    void findUserAverageScoreByBookId_ReturnsSuccess() {
        createBookProgress(book, user, 2);
        createBookProgress(book, user, 5);

        var average = repository.findUserAverageScoreByBookId(book.getId());

        assertNotNull(average);
        assertEquals(3.5, average, 0.001);
    }

    @Test
    void findUserAverageScoreByBookId_ReturnsFailure() {
        var average = repository.findUserAverageScoreByBookId(999L);
        assertNull(average);
    }

    @Test
    void updateIsFavoriteById_ReturnsSuccess() {
        var progress = createBookProgress(book, user, 3);
        boolean isFavorite = true;

        repository.updateIsFavoriteById(isFavorite, progress.getId());
        manager.flush();
        manager.clear();

        BookProgress updatedProgress = manager.find(BookProgress.class, progress.getId());
        assertTrue(updatedProgress.getIsFavorite());
    }

    @Test
    void updateIsFavoriteById_ReturnsFailure() {
        var progress = createBookProgress(book, user, 3);
        boolean isFavorite = true;

        repository.updateIsFavoriteById(isFavorite, 999L);
        manager.flush();
        manager.clear();

        BookProgress updatedProgress = manager.find(BookProgress.class, progress.getId());
        assertFalse(updatedProgress.getIsFavorite());
    }

    @BeforeEach
    void setUp() {
        book = createBook();
        user = createUser();
        createBookProgress(book, user, 4);
    }

    private BookProgress createBookProgress(Book book, User user, int score) {
        var progress = new BookProgress();
        progress.setBook(book);
        progress.setUser(user);
        progress.setScore(score);
        progress.setPagesRead(20);
        progress.setIsFavorite(false); // Valor padrão
        manager.persist(progress);
        manager.flush();
        return progress; // Retorna o progresso criado
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
        manager.persist(book);
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
        manager.persist(user);
        return user;
    }
}