package br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;

public class UserResponseFactory {

    private UserResponseFactory() {
    }

    public static UserResponse validResponse() {
        var user = UserFactory.validUser();
        var response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setEmail(user.getEmail());
        response.setDescription(user.getDescription());
        response.setRole(user.getRole());
        response.setIsEnabled(user.getIsEnabled());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setImage(null);
        return response;
    }

}
