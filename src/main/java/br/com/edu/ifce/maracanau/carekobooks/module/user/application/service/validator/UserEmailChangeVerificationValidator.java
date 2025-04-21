package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserEmailChangeVerificationValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isNotEnabled(user)) {
            throw new BadRequestException("User not verified");
        }

        if (isEmailResetTokenEmpty(user)) {
            throw new BadRequestException("Invalid token");
        }

        if (isEmailResetTokenExpired(user)) {
            throw new BadRequestException("Token expired");
        }
    }

    private boolean isNotEnabled(User user) {
        return !user.isEnabled();
    }

    private boolean isEmailResetTokenEmpty(User user) {
        return user.getEmailVerificationToken() == null;
    }

    private boolean isEmailResetTokenExpired(User user) {
        return user.getEmailVerificationTokenExpiresAt().isBefore(LocalDateTime.now());
    }

}
