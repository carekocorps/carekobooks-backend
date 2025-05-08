package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long>, JpaSpecificationExecutor<BookReview> {

    @Query("""
        SELECT AVG(br.score)
        FROM BookReview br
        WHERE br.book.id = :bookId
    """)
    Double calculateReviewAverageScore(Long bookId);

}
