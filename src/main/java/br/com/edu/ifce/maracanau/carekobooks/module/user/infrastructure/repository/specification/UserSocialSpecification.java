package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.UserRelationship;
import org.springframework.data.jpa.domain.Specification;

public class UserSocialSpecification {

    private UserSocialSpecification() {
    }

    public static Specification<User> relationshipUsernameEqual(String username, UserRelationship relationship) {
        return (root, query, cb) -> {
            var users = relationship == UserRelationship.FOLLOWING
                    ? root.get("followers")
                    : root.get("following");

            return cb.equal(users.get("username"), username);
        };
    }

    public static Specification<User> targetUsernameContains(String targetUsername) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("username")), "%" + targetUsername.toUpperCase() + "%");
    }

    public static Specification<User> targetDisplayNameContains(String targetDisplayName) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("displayName")), "%" + targetDisplayName.toUpperCase() + "%");
    }

}
