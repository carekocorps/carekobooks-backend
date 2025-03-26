package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> usernameContains(String username) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("username")), "%" + username.toUpperCase() + "%");
    }

}
