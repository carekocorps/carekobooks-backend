package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFactory {

    private static final Faker faker = new Faker();

    private UserFactory() {
    }

    public static User validUserWithNullId() {
        var user = new User();
        user.setId(null);
        user.setKeycloakId(UUID.randomUUID());
        user.setUsername(faker.internet().username().toLowerCase().replaceAll("[^a-z0-9]", ""));
        user.setDisplayName(faker.name().name());
        user.setDescription(faker.lorem().paragraph());
        user.setImage(null);
        user.setProgresses(List.of());
        user.setActivities(List.of());
        user.setReviews(List.of());
        user.setThreads(List.of());
        user.setReplies(List.of());
        user.setFollowing(List.of());
        user.setFollowers(List.of());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(user.getCreatedAt());
        return user;
    }

    public static User validUserWithNullIdAndFollowing(User target) {
        var user = validUserWithNullId();
        user.setFollowing(new ArrayList<>(user.getFollowing()));
        user.getFollowing().add(target);
        return user;
    }

    public static User validUserWithNullId(UUID keycloakId, UserSignUpRequest request) {
        var user = new User();
        user.setId(null);
        user.setKeycloakId(keycloakId);
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setDescription(request.getDescription());
        user.setImage(null);
        user.setProgresses(List.of());
        user.setActivities(List.of());
        user.setReviews(List.of());
        user.setThreads(List.of());
        user.setReplies(List.of());
        user.setFollowing(List.of());
        user.setFollowers(List.of());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(user.getCreatedAt());
        return user;
    }

    public static User validUserWithNullIdAndImage(Image image) {
        var user = validUserWithNullId();
        user.setImage(image);
        return user;
    }

    public static User validUser() {
        var user = validUserWithNullId();
        user.setId(faker.number().randomNumber());
        return user;
    }

    public static User validUser(String username) {
        var user = validUserWithNullId();
        user.setId(faker.number().randomNumber());
        user.setUsername(username);
        return user;
    }

    public static User validUser(UUID keycloakId, UserSignUpRequest request) {
        var user = validUserWithNullId(keycloakId, request);
        user.setId(faker.number().randomNumber());
        return user;
    }

    public static User validUserWithFollowing(User target) {
        var user = validUserWithNullIdAndFollowing(target);
        user.setId(faker.number().randomNumber());
        return user;
    }

    public static User validUserWithImage() {
        var user = validUserWithNullIdAndImage(ImageFactory.validImage());
        user.setId(faker.number().randomNumber());
        return user;
    }

}
