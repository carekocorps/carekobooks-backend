package br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import com.github.javafaker.Faker;

public class UserSignUpRequestFactory {

    private static final Faker faker = new Faker();

    private UserSignUpRequestFactory() {
    }

    public static UserSignUpRequest validRequest() {
        var user = UserFactory.validUser();
        var request = new UserSignUpRequest();
        request.setUsername(user.getUsername());
        request.setDisplayName(user.getDisplayName());
        request.setEmail(faker.internet().emailAddress());
        request.setDescription(user.getDescription());
        return request;
    }

    public static UserSignUpRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static UserSignUpRequest invalidRequestByUsernameBelowMinLength() {
        var request = validRequest();
        request.setUsername(faker.lorem().characters(1));
        return request;
    }

    public static UserSignUpRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername(faker.lorem().characters(51));
        return request;
    }

    public static UserSignUpRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example@name");
        return request;
    }

    public static UserSignUpRequest invalidRequestByDisplayNameExceedingMaxLength() {
        var request = validRequest();
        request.setDisplayName(faker.lorem().characters(51));
        return request;
    }

    public static UserSignUpRequest invalidRequestByBlankEmail() {
        var request = validRequest();
        request.setEmail(null);
        return request;
    }

    public static UserSignUpRequest invalidRequestByEmailExceedingMaxLength() {
        var request = validRequest();
        request.setEmail(faker.internet().emailAddress(faker.lorem().characters(256)));
        return request;
    }

    public static UserSignUpRequest invalidRequestByEmailRegexMismatch() {
        var request = validRequest();
        request.setEmail(faker.lorem().characters(10));
        return request;
    }

    public static UserSignUpRequest invalidRequestByDescriptionExceedingMaxLength() {
        var request = validRequest();
        request.setDescription(faker.lorem().characters(1001));
        return request;
    }

}
