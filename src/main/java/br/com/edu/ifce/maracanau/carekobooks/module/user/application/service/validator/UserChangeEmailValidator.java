package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.auth.AuthInvalidVerificationTokenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.auth.AuthVerificationTokenExpiredException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user.UserNotVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserChangeEmailValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isNotEnabled(user)) {
            throw new UserNotVerifiedException();
        }

        if (isOtpEmpty(user)) {
            throw new AuthInvalidVerificationTokenException();
        }

        if (isOtpValidationTypeInvalid(user)) {
            throw new AuthInvalidVerificationTokenException();
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
        return user.getOtpValidationType() == OtpValidationType.EMAIL;
    }

    private boolean isOtpExpired(User user) {
        return user.getOtpExpiresAt().isBefore(LocalDateTime.now());
    }

}
