package br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.springframework.web.util.UriComponentsBuilder;

public class UserQueryFactory {

    private UserQueryFactory() {
    }

    public static String validURIString(User user, String orderBy, boolean isAscendingOrder) {
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

}
