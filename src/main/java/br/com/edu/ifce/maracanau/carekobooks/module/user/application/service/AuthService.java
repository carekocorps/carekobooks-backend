package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth.AuthVerificationTokenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserAlreadyVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.content.enums.NotificationContentStrategy;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.content.factory.NotificationContentFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.subject.UserNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserChangeEmailValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserRecoverPasswordValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserRegisterVerificationValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public void login(UserLoginRequest request, HttpServletResponse response) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        tokenService.accessToken(user.getUsername(), user.getRoles(), response);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        tokenService.refreshToken(request, response);
    }

    @Transactional
    public void generateOtp(UserGenerateOtpRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        var isEnabled = Boolean.TRUE.equals(user.getIsEnabled());
        var isRegistrationOtp = user.getOtpValidationType() == OtpValidationType.REGISTRATION;
        if (!isEnabled && isRegistrationOtp) {
            throw new UserNotVerifiedException();
        }

        if (isEnabled && isRegistrationOtp) {
            throw new UserAlreadyVerifiedException();
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
                NotificationContentFactory.buildFromRegistrationOtp(user.getOtp())
        );

        return userMapper.toResponse(user);
    }

    @Transactional
    public void verify(UserRegisterVerificationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        userRegisterVerificationValidator.validate(user);
        if (!user.getOtp().equals(request.getOtp())) {
            throw new AuthVerificationTokenException();
        }

        userRepository.verifyByUsername(user.getUsername());
    }

    @Transactional
    public void recoverPassword(UserRecoverPasswordRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        userRecoverPasswordValidator.validate(user);
        if (!request.getOtp().equals(user.getOtp())) {
            throw new AuthVerificationTokenException();
        }

        userRepository.changePasswordByUsername(user.getUsername(), passwordEncoder.encode(request.getPassword()));
    }

    @Transactional
    public void changeEmail(UserChangeEmailRequest request) {
        var user = userRepository
                .findByEmail(request.getCurrentEmail())
                .orElseThrow(UserNotFoundException::new);

        userChangeEmailValidator.validate(user);
        if (!request.getOtp().equals(user.getOtp())) {
            throw new AuthVerificationTokenException();
        }

        userRepository.changeEmailByUsername(user.getUsername(), request.getNewEmail());
    }

}
