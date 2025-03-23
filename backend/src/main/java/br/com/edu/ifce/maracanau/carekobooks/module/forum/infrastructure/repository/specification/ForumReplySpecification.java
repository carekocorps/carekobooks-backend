package br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import org.springframework.data.jpa.domain.Specification;

public class ForumReplySpecification {

    public static Specification<ForumReply> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<ForumReply> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

}
