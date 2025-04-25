package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.enums.NotificationContentStrategy;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.factory.NotificationContentFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.subject.UserNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
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
    private final UserRecoverPasswordValidator userRecoverPasswordValidator;
    private final UserChangeEmailValidator userChangeEmailValidator;

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
    public void generateOtp(UserGenerateOtpRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        if (!Boolean.TRUE.equals(user.getIsEnabled()) && user.getOtpValidationType() != OtpValidationType.REGISTRATION) {
            throw new BadRequestException("User not verified");
        }

        var otp = UUID.randomUUID().toString().substring(0, 8);
        userNotificationSubject.notify(
                userMapper.toResponse(user),
                NotificationContentStrategy
                        .valueOf(request.getValidationType().toString())
                        .build(otp)
        );

        var otpExpiresAt = LocalDateTime.now().plusHours(1);
        userRepository.changeOtpValuesByUsername(user.getUsername(), otp, request.getValidationType(), otpExpiresAt);
    }

    @Transactional
    public UserResponse register(UserRegisterRequest request, MultipartFile image) {
        var user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsEnabled(false);
        user.setOtp(UUID.randomUUID().toString().substring(0, 8));
        user.setOtpValidationType(OtpValidationType.REGISTRATION);
        user.setOtpExpiresAt(LocalDateTime.now().plusHours(1));

        if (image != null) {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userValidator.validate(user);
        userRepository.save(user);
        userNotificationSubject.notify(
                userMapper.toResponse(user),
                NotificationContentFactory.fromRegistrationOtp(user.getOtp())
        );

        return userMapper.toResponse(user);
    }

    @Transactional
    public void verify(UserRegisterVerificationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userRegisterVerificationValidator.validate(user);
        if (!user.getOtp().equals(request.getOtp())) {
            throw new BadRequestException("Invalid otp");
        }

        userRepository.verifyByUsername(user.getUsername());
    }

    @Transactional
    public void recoverPassword(UserRecoverPasswordRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userRecoverPasswordValidator.validate(user);
        if (!request.getOtp().equals(user.getOtp())) {
            throw new BadRequestException("Invalid otp");
        }

        userRepository.changePasswordByUsername(user.getUsername(), passwordEncoder.encode(request.getPassword()));
    }

    @Transactional
    public void changeEmail(UserChangeEmailRequest request) {
        var user = userRepository
                .findByEmail(request.getCurrentEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        userChangeEmailValidator.validate(user);
        if (!request.getOtp().equals(user.getOtp())) {
            throw new BadRequestException("Invalid verification token");
        }

        userRepository.changeEmailByUsername(user.getUsername(), request.getNewEmail());
    }

    public TokenResponse refreshToken(String username, UserRefreshTokenRequest request) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("Username not found");
        }

        return tokenService.refreshToken(request.getRefreshToken());
    }

}
