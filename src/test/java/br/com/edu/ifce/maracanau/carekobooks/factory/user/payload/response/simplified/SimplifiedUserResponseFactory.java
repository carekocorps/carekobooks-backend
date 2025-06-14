package br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.simplified;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;

public class SimplifiedUserResponseFactory {

    private SimplifiedUserResponseFactory() {
    }

    public static SimplifiedUserResponse validResponse(User user) {
        var response = new SimplifiedUserResponse();
        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setImage(null);
        return response;
    }

}
