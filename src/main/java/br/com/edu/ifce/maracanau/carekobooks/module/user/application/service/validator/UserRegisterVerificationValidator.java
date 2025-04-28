package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth.AuthVerificationTokenTypeException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth.AuthVerificationTokenExpiredException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserAlreadyVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserRegisterVerificationValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isEnabled(user)) {
            throw new UserAlreadyVerifiedException();
        }

        if (isOtpValidationTypeInvalid(user)) {
            throw new AuthVerificationTokenTypeException();
        }

        if (isOtpExpired(user)) {
            throw new AuthVerificationTokenExpiredException();
        }
    }

    private boolean isEnabled(User user) {
        return user.isEnabled();
    }

    private boolean isOtpValidationTypeInvalid(User user) {
        return user.getOtpValidationType() != OtpValidationType.REGISTRATION;
    }

    private boolean isOtpExpired(User user) {
        return user.getOtpExpiresAt().isBefore(LocalDateTime.now());
    }

}
