package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import org.springframework.data.jpa.domain.Specification;

public class BookProgressSpecification {

    private BookProgressSpecification() {
    }

    public static Specification<BookProgress> statusEqual(BookProgressStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<BookProgress> isFavoriteEqual(Boolean isFavorite) {
        return (root, query, cb) ->
                cb.equal(root.get("isFavorite"), isFavorite);
    }

    public static Specification<BookProgress> scoreEqual(Integer score) {
        return (root, query, cb) ->
                cb.equal(root.get("score"), score);
    }

    public static Specification<BookProgress> pagesReadEqual(Integer pagesRead) {
        return (root, query, cb) ->
                cb.equal(root.get("pagesRead"), pagesRead);
    }

    public static Specification<BookProgress> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<BookProgress> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

}
