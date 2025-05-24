package br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class UserFactory {

    private UserFactory() {
    }

    public static User validUser() {
        var random = new Random();
        var user = new User();
        user.setId(random.nextLong());
        user.setUsername(UUID.randomUUID().toString().replace("-", ""));
        user.setPassword(UUID.randomUUID().toString());
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(user.getCreatedAt());
        return user;
    }

    public static User validUser(String username) {
        var user = validUser();
        user.setUsername(username);
        return user;
    }

}
