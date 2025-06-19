package br.com.edu.ifce.maracanau.carekobooks.factory.module.user.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.springframework.web.util.UriComponentsBuilder;

public class UserUriFactory {

    private UserUriFactory() {
    }

    public static String validUri(String username) {
        return UriComponentsBuilder
                .fromPath("/api/v1/users")
                .pathSegment(username)
                .build()
                .toUriString();
    }

    public static String validQueryUri(User user, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/users")
                .queryParam("username", user.getUsername())
                .queryParam("displayName", user.getDisplayName())
                .queryParam("createdBefore", user.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", user.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

    public static String validSocialFollowingQueryUri(User userFollowing, User userFollowed, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/users")
                .pathSegment(userFollowing.getUsername(), "social", "following")
                .queryParam("targetUsername", userFollowed.getUsername())
                .queryParam("targetDisplayName", userFollowed.getDisplayName())
                .queryParam("createdBefore", userFollowed.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", userFollowed.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

    public static String validSocialFollowersQueryUri(User userFollowing, User userFollowed, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/users")
                .pathSegment(userFollowed.getUsername(), "social", "followers")
                .queryParam("targetUsername", userFollowing.getUsername())
                .queryParam("targetDisplayName", userFollowing.getDisplayName())
                .queryParam("createdBefore", userFollowing.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", userFollowing.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

}
