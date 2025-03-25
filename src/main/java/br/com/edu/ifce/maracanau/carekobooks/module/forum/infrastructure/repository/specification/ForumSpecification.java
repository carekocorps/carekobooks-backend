package br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import org.springframework.data.jpa.domain.Specification;

public class ForumSpecification {

    public static Specification<Forum> titleContains(String title) {
        return (root, query, cb) ->
                cb.equal(cb.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Forum> usernameEqual(String username) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<Forum> bookIdEqual(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

}
