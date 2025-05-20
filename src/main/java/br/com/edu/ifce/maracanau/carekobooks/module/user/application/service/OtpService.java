package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.content.enums.NotificationContentStrategy;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.subject.UserNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.OtpValidationType;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.auth.AuthVerificationTokenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.auth.AuthVerificationTokenExpiredException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserAlreadyVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotVerifiedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OtpService {

    private final UserMapper userMapper;
    private final UserNotificationSubject userNotificationSubject;

    public String generateOtp() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public LocalDateTime generateOtpExpiresAt() {
        return LocalDateTime.now().plusHours(1);
    }

    public void notify(User user, String otp, OtpValidationType otpValidationType) {
        if (user.isEnabled() && otpValidationType == OtpValidationType.REGISTRATION) {
            throw new UserAlreadyVerifiedException();
        }

        if (!user.isEnabled() && otpValidationType != OtpValidationType.REGISTRATION) {
            throw new UserNotVerifiedException();
        }

        userNotificationSubject.notify(
                userMapper.toResponse(user),
                NotificationContentStrategy
                        .valueOf(otpValidationType.toString())
                        .build(otp)
        );
    }

    public void verify(User user, String otp, OtpValidationType otpValidationType) {
        if (user.isEnabled() && otpValidationType == OtpValidationType.REGISTRATION) {
            throw new UserAlreadyVerifiedException();
        }

        if (!user.isEnabled() && otpValidationType != OtpValidationType.REGISTRATION) {
            throw new UserNotVerifiedException();
        }

        if (user.getOtp() == null || user.getOtpValidationType() != otpValidationType || !user.getOtp().equals(otp)) {
            throw new AuthVerificationTokenException();
        }

        if (user.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthVerificationTokenExpiredException();
        }
    }

}
