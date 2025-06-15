package br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UserSocialQueryFactory {

    private UserSocialQueryFactory() {
    }

    public static URI validFollowingURI(User userFollowing, User userFollowed, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/users/" + userFollowing.getUsername() + "/social/following")
//                .queryParam("targetUsername", userFollowed.getUsername())
//                .queryParam("targetDisplayName", userFollowed.getDisplayName())
                .queryParam("createdBefore", userFollowed.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", userFollowed.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

    public static URI validFollowersURI(User userFollowing, User userFollowed, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/users/" + userFollowed.getUsername() + "/social/followers")
//                .queryParam("targetUsername", userFollowing.getUsername())
//                .queryParam("targetDisplayName", userFollowing.getDisplayName())
                .queryParam("createdBefore", userFollowing.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", userFollowing.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
