package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import org.springframework.data.jpa.domain.Specification;

public class BookActivitySpecification {

    public static Specification<BookActivity> statusEqual(BookProgressStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<BookActivity> pagesReadEqual(Integer pagesRead) {
        return (root, query, cb) ->
                cb.equal(root.get("pagesRead"), pagesRead);
    }

    public static Specification<BookActivity> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<BookActivity> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

}
