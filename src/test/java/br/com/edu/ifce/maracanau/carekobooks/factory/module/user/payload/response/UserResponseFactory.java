package br.com.edu.ifce.maracanau.carekobooks.factory.module.user.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;

public class UserResponseFactory {

    private UserResponseFactory() {
    }

    public static UserResponse validResponse(User user) {
        var response = new UserResponse();
        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setDescription(user.getDescription());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setImage(null);
        return response;
    }

}
