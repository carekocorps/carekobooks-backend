package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import net.datafaker.Faker;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.UUID;

public class UserRepresentationFactory {

    private static final Faker faker = new Faker();

    private UserRepresentationFactory() {
    }

    public static UserRepresentation validRepresentationWithNullId() {
        var user = UserFactory.validUserWithNullId();
        var representation = new UserRepresentation();
        representation.setUsername(user.getUsername());
        representation.setEmail(faker.internet().emailAddress());
        representation.setEnabled(true);
        representation.setEmailVerified(true);
        return representation;
    }

    public static UserRepresentation validRepresentation() {
        var representation = validRepresentationWithNullId();
        representation.setId(UUID.randomUUID().toString());
        return representation;
    }

}
