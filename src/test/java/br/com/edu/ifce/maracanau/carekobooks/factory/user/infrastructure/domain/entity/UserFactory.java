package br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import com.github.javafaker.Faker;

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
        user.setUsername(faker.name().username().toLowerCase().replaceAll("[^a-z0-9]", ""));
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

    public static User validUserWithNullIdAndFollowing(User target) {
        var user = validUserWithNullId();
        user.setFollowing(new ArrayList<>(user.getFollowing()));
        user.getFollowing().add(target);
        return user;
    }

}
