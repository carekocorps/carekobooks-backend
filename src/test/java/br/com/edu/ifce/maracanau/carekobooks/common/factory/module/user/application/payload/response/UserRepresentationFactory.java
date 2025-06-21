package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.UUID;

public class UserRepresentationFactory {

    private UserRepresentationFactory() {
    }

    public static UserRepresentation validRepresentation(UserSignUpRequest request) {
        var representation = new UserRepresentation();
        representation.setId(UUID.randomUUID().toString());
        representation.setUsername(request.getUsername());
        representation.setEmail(request.getEmail());
        representation.setEnabled(true);
        representation.setEmailVerified(false);
        return representation;
    }

    public static UserRepresentation validRepresentation() {
        return validRepresentation(UserSignUpRequestFactory.validRequest());
    }

    public static UserRepresentation validRepresentationWithEmailVerified() {
        var representation = validRepresentation();
        representation.setEmailVerified(true);
        return representation;
    }

}
