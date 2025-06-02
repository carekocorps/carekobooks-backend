package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
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

    public static Specification<BookProgress> pageCountEqual(Integer pageCount) {
        return (root, query, cb) ->
                cb.equal(root.get("pageCount"), pageCount);
    }

    public static Specification<BookProgress> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<BookProgress> genreEqual(String genre) {
        return (root, query, cb) ->
                cb.equal(root.join("book").join("genres").get("name"), genre);
    }

    public static Specification<BookProgress> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

}
