package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ConflictException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.subject.UserNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.*;
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
    private final UserMapper userMapper;
    private final UserNotificationSubject userNotificationSubject;

    private final UserRegisterVerificationValidator userRegisterVerificationValidator;
    private final UserPasswordRecoveryVerificationValidator userPasswordRecoveryVerificationValidator;
    private final UserEmailChangeVerificationValidator userEmailChangeVerificationValidator;

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
    public UserResponse initRegistration(UserRegisterInitializationRequest request, MultipartFile image) {
        var user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsEnabled(false);
        user.setVerificationToken(UUID.randomUUID().toString().substring(0, 8));
        user.setVerificationTokenExpiresAt(LocalDateTime.now().plusHours(1));

        if (image != null) {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userValidator.validate(user);
        userRepository.save(user);
        userNotificationSubject.notify(
                userMapper.toResponse(user),
                NotificationContent.fromVerificationToken(user.getVerificationToken())
        );

        return userMapper.toResponse(user);
    }

    @Transactional
    public void verifyRegistration(UserRegisterVerificationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userRegisterVerificationValidator.validate(user);
        if (!user.getVerificationToken().equals(request.getVerificationToken())) {
            throw new BadRequestException("Invalid verification token");
        }

        userRepository.verifyRegistrationByUsername(user.getUsername());
    }

    @Transactional
    public void initPasswordRecovery(UserPasswordRecoveryInitializationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        if (!user.isEnabled()) {
            throw new ForbiddenException("User not verified");
        }

        var passwordVerificationToken = UUID.randomUUID().toString().substring(0, 8);
        var passwordVerificationTokenExpiresAt = LocalDateTime.now().plusHours(1);
        userRepository.initPasswordRecoveryByUsername(user.getUsername(), passwordVerificationToken, passwordVerificationTokenExpiresAt);
        userNotificationSubject.notify(
                userMapper.toResponse(user),
                NotificationContent.fromPasswordVerificationToken(passwordVerificationToken)
        );
    }

    @Transactional
    public void verifyPasswordRecovery(UserPasswordRecoveryVerificationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userPasswordRecoveryVerificationValidator.validate(user);
        if (!user.getPasswordVerificationToken().equals(request.getPasswordResetToken())) {
            throw new BadRequestException("Invalid verification token");
        }

        var encodedPassword = passwordEncoder.encode(request.getPassword());
        userRepository.verifyPasswordRecoveryByUsername(user.getUsername(), encodedPassword);
    }

    @Transactional
    public void initEmailChange(UserEmailChangeInitializationRequest request) {
        var user = userRepository
                .findByEmail(request.getCurrentEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        if (!user.isEnabled()) {
            throw new ForbiddenException("User not verified");
        }

        if (user.getEmail().equals(request.getNewEmail())) {
            throw new BadRequestException("New email must be different from current email");
        }

        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new ConflictException("New email is already taken");
        }

        var emailVerificationToken = UUID.randomUUID().toString().substring(0, 8);
        var emailVerificationTokenExpiresAt = LocalDateTime.now().plusHours(1);
        userRepository.initEmailChangeByUsername(user.getUsername(), emailVerificationToken, emailVerificationTokenExpiresAt);

        var userNotified = userMapper.toResponse(user);
        userNotified.setEmail(request.getNewEmail());
        userNotificationSubject.notify(userNotified, NotificationContent.fromEmailVerificationToken(emailVerificationToken));
    }

    @Transactional
    public void verifyEmailChange(UserEmailChangeVerificationRequest request) {
        var user = userRepository
                .findByEmail(request.getCurrentEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userEmailChangeVerificationValidator.validate(user);
        if (!user.getEmailVerificationToken().equals(request.getEmailResetToken())) {
            throw new BadRequestException("Invalid verification token");
        }

        var newEmail = request.getNewEmail();
        userRepository.verifyEmailChangeByUsername(user.getUsername(), newEmail);
    }

    public TokenResponse refreshToken(String username, UserTokenRefreshRequest request) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("Username not found");
        }

        return tokenService.refreshToken(request.getRefreshToken());
    }

}
