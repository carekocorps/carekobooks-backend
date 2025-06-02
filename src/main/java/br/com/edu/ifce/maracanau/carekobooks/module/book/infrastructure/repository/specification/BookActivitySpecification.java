package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
import org.springframework.data.jpa.domain.Specification;

public class BookActivitySpecification {

    private BookActivitySpecification() {
    }

    public static Specification<BookActivity> statusEqual(BookProgressStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<BookActivity> pageCountEqual(Integer pageCount) {
        return (root, query, cb) ->
                cb.equal(root.get("pageCount"), pageCount);
    }

    public static Specification<BookActivity> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<BookActivity> genreEquals(String genre) {
        return (root, query, cb) ->
                cb.equal(root.join("book").join("genres").get("name"), genre);
    }

    public static Specification<BookActivity> followerUsernameEqual(String username) {
        return (root, query, cb) -> {
            var users = root.get("user").get("followers");
            return cb.equal(users.get("username"), username);
        };
    }

    public static Specification<BookActivity> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

}
