package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;

import java.util.Optional;

public class SimplifiedUserResponseFactory {

    private SimplifiedUserResponseFactory() {
    }

    public static SimplifiedUserResponse validResponse(User user) {
        var response = new SimplifiedUserResponse();
        Optional.ofNullable(user.getImage()).ifPresent(x -> response.setImage(ImageResponseFactory.validResponse(x)));
        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

}
