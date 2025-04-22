package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    boolean existsById(Long id);

    @Transactional
    @Modifying
    @Query("""
        UPDATE Book b
        SET b.userAverageScore = :userAverageScore
        WHERE b.id = :id
    """)
    void updateUserAverageScoreById(Double userAverageScore, Long id);

    @Transactional
    @Modifying
    @Query("""
        UPDATE Book b
        SET b.reviewAverageScore = :reviewAverageScore
        WHERE b.id = :id
    """)
    void updateReviewAverageScoreById(Double reviewAverageScore, Long id);

    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO `book_genre_relationships` (`book_id`, `book_genre_id`) VALUES
            (:bookId, :genreId);
    """, nativeQuery = true)
    void addGenre(Long bookId, Long genreId);

    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM `book_genre_relationships`
        WHERE `book_id` = :bookId
        AND `book_genre_id` = :genreId
    """, nativeQuery = true)
    void removeGenre(Long bookId, Long genreId);

}
