package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.subject.UserNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserPasswordRecoveryValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserVerificationValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserVerificationValidator userVerificationValidator;
    private final UserPasswordRecoveryValidator userPasswordRecoveryValidator;
    private final UserMapper userMapper;
    private final UserNotificationSubject userNotificationSubject;

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    public TokenResponse login(UserLoginRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!user.isEnabled()) {
            throw new ForbiddenException("User not verified");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
        );

        return tokenService.accessToken(user.getUsername(), user.getRoles());
    }

    @Transactional
    public UserResponse register(UserRegistrationRequest request, MultipartFile image) throws Exception {
        var user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsEnabled(false);
        user.setVerificationToken(UUID.randomUUID());
        user.setVerificationTokenExpiresAt(LocalDateTime.now().plusHours(1));

        if (image != null) {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userValidator.validate(user);
        userRepository.save(user);
        userNotificationSubject.notify(user, NotificationContent.fromVerificationToken(user.getVerificationToken()));
        return userMapper.toResponse(user);
    }

    @Transactional
    public void verify(UserVerificationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userVerificationValidator.validate(user);
        if (!user.getVerificationToken().equals(request.getVerificationCode())) {
            throw new BadRequestException("Invalid verification token");
        }

        userRepository.verifyUserByEmail(user.getEmail());
    }

    @Transactional
    public void forgotPassword(UserPasswordRecoveryRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        if (!user.isEnabled()) {
            throw new ForbiddenException("User not verified");
        }

        user.setResetToken(UUID.randomUUID());
        user.setResetTokenExpiresAt(LocalDateTime.now().plusHours(1));
        userRepository.updateResetTokenByEmail(user.getEmail(), user.getResetToken(), user.getResetTokenExpiresAt());
        userNotificationSubject.notify(user, NotificationContent.fromResetToken(user.getResetToken()));
    }

    @Transactional
    public void resetPassword(UserPasswordResetRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userPasswordRecoveryValidator.validate(user);
        if (!user.getResetToken().equals(request.getResetToken())) {
            throw new BadRequestException("Invalid verification token");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.resetPasswordByEmail(user.getEmail(), user.getPassword());
    }

    public TokenResponse refresh(String username, UserTokenRefreshRequest request) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("Username not found");
        }

        return tokenService.refreshToken(request.getRefreshToken());
    }

}
