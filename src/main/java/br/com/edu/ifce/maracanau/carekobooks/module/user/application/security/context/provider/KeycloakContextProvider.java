package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserUnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KeycloakContextProvider {

    private KeycloakContextProvider() {
    }

    public static void assertAuthorized(UUID expectedKeycloakId, Class<? extends RuntimeException> exception) {
        var jwt = getCurrentJwt();
        var keycloakId = UUID.fromString(jwt.getSubject());
        if (!expectedKeycloakId.equals(keycloakId) && !hasAdminRole(jwt)) {
            try {
                throw exception.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                throw new UserForbiddenException();
            }
        }
    }

    private static boolean hasAdminRole(Jwt jwt) {
        Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
        return realmAccess.get("roles").contains("admin");
    }

    private static Jwt getCurrentJwt() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserUnauthorizedException();
        }

        return (Jwt) authentication.getCredentials();
    }

}
