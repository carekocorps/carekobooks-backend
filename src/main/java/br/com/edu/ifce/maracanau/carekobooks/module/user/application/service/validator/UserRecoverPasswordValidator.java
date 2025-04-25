package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserRecoverPasswordValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isNotEnabled(user)) {
            throw new BadRequestException("User not verified");
        }

        if (isOtpEmpty(user)) {
            throw new BadRequestException("Invalid otp");
        }

        if (isOtpValidationTypeInvalid(user)) {
            throw new BadRequestException("Invalid otp type");
        }

        if (isOtpExpired(user)) {
            throw new BadRequestException("Otp expired");
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
