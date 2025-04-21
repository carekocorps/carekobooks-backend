package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserPasswordRecoveryVerificationValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isNotEnabled(user)) {
            throw new BadRequestException("User not verified");
        }

        if (isPasswordResetTokenEmpty(user)) {
            throw new BadRequestException("Invalid token");
        }

        if (isPasswordResetTokenExpired(user)) {
            throw new BadRequestException("Token expired");
        }
    }

    private boolean isNotEnabled(User user) {
        return !user.isEnabled();
    }

    private boolean isPasswordResetTokenEmpty(User user) {
        return user.getPasswordVerificationToken() == null;
    }

    private boolean isPasswordResetTokenExpired(User user) {
        return user.getPasswordVerificationTokenExpiresAt().isBefore(LocalDateTime.now());
    }

}
