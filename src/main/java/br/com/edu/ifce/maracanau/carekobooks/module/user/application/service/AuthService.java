package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.annotation.AuthenticatedUserMatchRequired;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.validator.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.OtpValidationType;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.*;
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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final OtpService otpService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    public void signin(UserSignInRequest request, HttpServletResponse response) {
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
    public UserResponse signup(UserSignUpRequest request, MultipartFile image, HttpServletResponse response) {
        var user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsEnabled(false);
        user.setOtp(otpService.generateOtp());
        user.setOtpValidationType(OtpValidationType.REGISTRATION);
        user.setOtpExpiresAt(otpService.generateOtpExpiresAt());

        if (image != null) {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userValidator.validate(user);
        userRepository.save(user);
        otpService.notify(user, user.getOtp(), OtpValidationType.REGISTRATION);
        return userMapper.toResponse(user);
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "username", exception = UserModificationForbiddenException.class)
    public void verifyOtp(UserOtpVerificationRequest request, HttpServletResponse response) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        otpService.verify(user, request.getOtp(), request.getValidationType());
        switch (request.getValidationType()) {
            case REGISTRATION -> userRepository.verifyUserByUsername(user.getUsername());
            case PASSWORD -> userRepository.verifyPasswordOtpByUsername(user.getUsername(), user.getTempPassword());
            case EMAIL -> userRepository.verifyEmailOtpByUsername(user.getUsername(), user.getTempEmail());
        }

        if (request.getValidationType() == OtpValidationType.REGISTRATION) {
            tokenService.accessToken(user.getUsername(), user.getRoles(), response);
        }
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "request", exception = UserModificationForbiddenException.class)
    public void recoverPassword(UserPasswordRecoveryRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException();
        }

        var otp = otpService.generateOtp();
        otpService.notify(user, otp, OtpValidationType.PASSWORD);
        userRepository.changeTempPasswordValuesByUsername(user.getUsername(), user.getPassword(), otp, otpService.generateOtpExpiresAt());
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "request", exception = UserModificationForbiddenException.class)
    public void changeEmail(UserEmailChangeRequest request) {
        if (request.getEmail().equals(request.getNewEmail())) {
            throw new UserEmailConflictException("The current email should be different from the new email");
        }

        var users = userRepository
                .findByEmailIn(List.of(request.getEmail(), request.getNewEmail()))
                .stream()
                .collect(Collectors.toMap(User::getEmail, user -> user));

        var user = users.get(request.getEmail());
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException();
        }

        var existingUser = users.get(request.getNewEmail());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new UserEmailConflictException();
        }

        var otp = otpService.generateOtp();
        otpService.notify(user, otp, OtpValidationType.EMAIL);
        userRepository.changeTempEmailValuesByEmail(request.getEmail(), request.getNewEmail(), otp, otpService.generateOtpExpiresAt());
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "request", exception = UserModificationForbiddenException.class)
    public void changeUsername(UserUsernameChangeRequest request, HttpServletResponse response) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException();
        }

        var existingUser = userRepository.findByUsername(request.getNewUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new UserUsernameConflictException();
        }

        userRepository.changeUsernameByUsername(request.getUsername(), request.getNewUsername());
        tokenService.accessToken(request.getNewUsername(), user.getRoles(), response);
    }

}
