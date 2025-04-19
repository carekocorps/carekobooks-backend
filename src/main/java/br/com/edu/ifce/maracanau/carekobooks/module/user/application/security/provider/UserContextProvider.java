package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.UnauthorizedException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserContextProvider {

    public static UserDetails getUserDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        return (UserDetails) authentication.getPrincipal();
    }

    public static boolean isUserUnauthorized(String authorizedUsername) {
        var userDetails = getUserDetails();
        var isUsernameMatching = userDetails.getUsername().equals(authorizedUsername);
        var isAdmin = userDetails
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + UserRole.ADMIN.name()));

        return !isUsernameMatching && !isAdmin;
    }

}
