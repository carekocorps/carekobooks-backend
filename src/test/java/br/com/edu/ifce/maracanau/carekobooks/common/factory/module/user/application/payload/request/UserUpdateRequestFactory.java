package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import com.github.javafaker.Faker;

public class UserUpdateRequestFactory {

    private static final Faker faker = new Faker();

    private UserUpdateRequestFactory() {
    }

    public static UserUpdateRequest validRequest() {
        var user = UserFactory.validUser();
        var request = new UserUpdateRequest();
        request.setUsername(user.getUsername());
        request.setDisplayName(user.getDisplayName());
        request.setDescription(user.getDescription());
        return request;
    }

    public static UserUpdateRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static UserUpdateRequest invalidRequestByUsernameBelowMinLength() {
        var request = validRequest();
        request.setUsername(faker.lorem().characters(1));
        return request;
    }

    public static UserUpdateRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername(faker.lorem().characters(51));
        return request;
    }

    public static UserUpdateRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example@name");
        return request;
    }

    public static UserUpdateRequest invalidRequestByDisplayNameExceedingMaxLength() {
        var request = validRequest();
        request.setDisplayName(faker.lorem().characters(51));
        return request;
    }

    public static UserUpdateRequest invalidRequestByDescriptionExceedingMaxLength() {
        var request = validRequest();
        request.setDescription(faker.lorem().characters(1001));
        return request;
    }

}
