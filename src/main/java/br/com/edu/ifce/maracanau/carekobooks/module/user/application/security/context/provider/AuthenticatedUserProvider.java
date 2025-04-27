package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.UnauthorizedException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUserProvider {

    private AuthenticatedUserProvider() {
    }

    public static User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        return (User) authentication.getPrincipal();
    }

    public static boolean isAuthenticatedUserUnauthorized(String authorizedUsername) {
        var userDetails = getAuthenticatedUser();
        var isUsernameMatching = userDetails.getUsername().equals(authorizedUsername);
        var isAdmin = userDetails
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + UserRole.ADMIN.name()));

        return !isUsernameMatching && !isAdmin;
    }

}
