package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> usernameContains(String username) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("username")), "%" + username.toUpperCase() + "%");
    }

    public static Specification<User> displayNameContains(String displayName) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("displayName")), "%" + displayName.toUpperCase() + "%");
    }

    public static Specification<User> isEnabledEquals(Boolean isEnabled) {
        return (root, query, cb) ->
                cb.equal(root.get("isEnabled"), isEnabled);
    }

}
