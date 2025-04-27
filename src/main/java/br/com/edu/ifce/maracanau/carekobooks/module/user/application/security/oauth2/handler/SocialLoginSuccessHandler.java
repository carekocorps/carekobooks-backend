package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.oauth2.handler;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.TokenService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class SocialLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        var oauth2User = (OAuth2User) authentication.getPrincipal();
        var email = (String) oauth2User.getAttribute("email");
        var user = userRepository
                .findByEmail(email)
                .orElseGet(() -> userRepository.save(createOAuth2UserFromEmail(email)));

        var jwt = tokenService.accessToken(user.getUsername(), user.getRoles());
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(jwt));
    }

    private User createOAuth2UserFromEmail(String email) {
        var user = new User();
        user.setUsername(UUID.randomUUID().toString().replace("-", ""));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setEmail(email);
        user.setIsEnabled(true);
        return user;
    }

}
