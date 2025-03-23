package br.com.edu.ifce.maracanau.carekobooks.module.user.shared;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUser {

    public static User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        return (User) authentication.getPrincipal();
    }

    public static boolean isAuthorOrAdmin(Long authorId) {
        var user = getAuthenticatedUser();
        return user.getId().equals(authorId) || user.getRole() == UserRole.ADMIN;
    }

}
