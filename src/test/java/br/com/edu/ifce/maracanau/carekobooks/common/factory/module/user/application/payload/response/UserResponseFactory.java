package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;

import java.util.Optional;

public class UserResponseFactory {

    private UserResponseFactory() {
    }

    public static UserResponse validResponse(User user) {
        var response = new UserResponse();
        Optional.ofNullable(user.getImage()).ifPresent(x -> response.setImage(ImageResponseFactory.validResponse(x)));
        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setDescription(user.getDescription());
        response.setProgressesCount(Optional.ofNullable(user.getProgressesCount()).orElse(0));
        response.setReviewsCount(Optional.ofNullable(user.getReviewsCount()).orElse(0));
        response.setRepliesCount(Optional.ofNullable(user.getRepliesCount()).orElse(0));
        response.setThreadsCount(Optional.ofNullable(user.getThreadsCount()).orElse(0));
        response.setFollowingCount(Optional.ofNullable(user.getFollowingCount()).orElse(0));
        response.setFollowersCount(Optional.ofNullable(user.getFollowersCount()).orElse(0));
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

}
