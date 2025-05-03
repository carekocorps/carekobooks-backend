package br.com.edu.ifce.maracanau.carekobooks.module.user.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth.AuthVerificationTokenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth.AuthVerificationTokenExpiredException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserRecoverPasswordValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isNotEnabled(user)) {
            throw new UserNotVerifiedException();
        }

        if (isOtpEmpty(user)) {
            throw new AuthVerificationTokenException();
        }

        if (isOtpValidationTypeInvalid(user)) {
            throw new AuthVerificationTokenException();
        }

        if (isOtpExpired(user)) {
            throw new AuthVerificationTokenExpiredException();
        }
    }

    private boolean isNotEnabled(User user) {
        return !user.isEnabled();
    }

    private boolean isOtpEmpty(User user) {
        return user.getOtp() == null;
    }

    private boolean isOtpValidationTypeInvalid(User user) {
        return user.getOtpValidationType() == OtpValidationType.PASSWORD;
    }

    private boolean isOtpExpired(User user) {
        return user.getOtpExpiresAt().isBefore(LocalDateTime.now());
    }

}
