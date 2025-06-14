package br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserFactory {

    private static final Faker faker = new Faker();

    private UserFactory() {
    }

    public static User validUser() {
        var user = new User();
        user.setId(faker.number().randomNumber());
        user.setKeycloakId(UUID.randomUUID());
        user.setUsername(faker.name().username().toLowerCase().replaceAll("[^a-z0-9]", ""));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(user.getCreatedAt());
        return user;
    }

    public static User validUser(String username) {
        var user = validUser();
        user.setUsername(username);
        return user;
    }

    public static User validUserWithNullId() {
        var user = validUser();
        user.setId(null);
        return user;
    }

}
