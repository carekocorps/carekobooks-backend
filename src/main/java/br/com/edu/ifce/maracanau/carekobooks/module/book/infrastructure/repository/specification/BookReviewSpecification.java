package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import org.springframework.data.jpa.domain.Specification;

public class BookReviewSpecification {

    public static Specification<BookReview> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<BookReview> scoreEqual(Integer score) {
        return (root, query, cb) ->
                cb.equal(root.get("score"), score);
    }

    public static Specification<BookReview> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

}
