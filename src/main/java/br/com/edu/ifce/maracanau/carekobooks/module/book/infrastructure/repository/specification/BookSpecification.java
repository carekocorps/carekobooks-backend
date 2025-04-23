package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Book> authorContains(String author) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("author")), "%" + author.toUpperCase() + "%");
    }

    public static Specification<Book> publisherContains(String publisher) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("publisher")), "%" + publisher.toUpperCase() + "%");
    }

    public static Specification<Book> publishedBefore(LocalDate publishedBefore) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("publishedAt"), publishedBefore);
    }

    public static Specification<Book> publishedAfter(LocalDate publishedAfter) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("publishedAt"), publishedAfter);
    }

    public static Specification<Book> totalPagesEqual(Integer totalPages) {
        return (root, query, cb) ->
                cb.equal(root.get("totalPages"), totalPages);
    }

    public static Specification<Book> genreEquals(String genre) {
        return (root, query, cb) ->
                cb.equal(root.join("genres").get("name"), genre);
    }

}
