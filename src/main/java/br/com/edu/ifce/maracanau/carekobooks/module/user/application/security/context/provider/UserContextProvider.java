package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserUnauthorizedException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserContextProvider {

    private final UserRepository userRepository;

    public void assertAuthorized(String expectedUsername, Class<? extends RuntimeException> exception) {
        var jwt = getCurrentJwt();
        var user = userRepository
                .findByKeycloakId(UUID.fromString(jwt.getSubject()))
                .orElseThrow(UserNotFoundException::new);

        if (!expectedUsername.equals(user.getUsername()) && !hasAdminRole(jwt)) {
            try {
                throw exception.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new UserUnauthorizedException();
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
