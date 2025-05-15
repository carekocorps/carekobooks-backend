package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSocialSpecification {

    private UserSocialSpecification() {
    }

    public static Specification<User> followingUsernameEqual(String username) {
        return (root, query, cb) -> {
            var users = root.get("followers");
            return cb.equal(users.get("username"), username);
        };
    }

    public static Specification<User> followersUsernameEqual(String username) {
        return (root, query, cb) -> {
            var users = root.get("following");
            return cb.equal(users.get("username"), username);
        };
    }

}
