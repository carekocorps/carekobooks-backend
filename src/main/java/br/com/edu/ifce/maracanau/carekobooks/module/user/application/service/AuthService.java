package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserLoginRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public TokenResponse login(UserLoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return tokenService.createAccessToken(
                user.getUsername(),
                user.getRoles()
        );
    }

    public TokenResponse refreshToken(String username, String refreshToken) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("Username not found");
        }

        return tokenService.refreshToken(refreshToken);
    }

    @Transactional
    public TokenResponse register(UserRegisterRequest request) throws Exception {
        var user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userValidator.validate(user);
        userRepository.save(user);

        return tokenService.createAccessToken(
                user.getUsername(),
                user.getRoles()
        );
    }

}
