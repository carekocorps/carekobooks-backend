package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Test
    void existsById() {
    }

    @Test
    void updateUserAverageScoreById() {
    }

    @Test
    void updateReviewAverageScoreById() {
    }
}