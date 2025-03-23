package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.dto.TokenDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.request.LoginRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.request.RegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    public TokenDTO login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return tokenService.createAccessToken(
                request.getUsername(),
                user.getRoles()
        );
    }

    public TokenDTO refreshToken(String username, String refreshToken) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("Username not found");
        }

        return tokenService.refreshToken(refreshToken);
    }

    public TokenDTO register(RegisterRequest request) {
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
