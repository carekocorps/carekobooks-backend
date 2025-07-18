package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import org.springframework.data.jpa.domain.Specification;

public class BookThreadReplySpecification {

    private BookThreadReplySpecification() {
    }

    public static Specification<BookThreadReply> threadIdEqual(Long threadId) {
        return (root, query, cb) ->
                cb.equal(root.get("thread").get("id"), threadId);
    }

    public static Specification<BookThreadReply> parentIdEqual(Long parentId) {
        return (root, query, cb) ->
                cb.equal(root.get("parent").get("id"), parentId);
    }

    public static Specification<BookThreadReply> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<BookThreadReply> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("thread").get("book").get("id"), bookId);
    }

}
