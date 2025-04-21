package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserPasswordRecoveryValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isNotEnabled(user)) {
            throw new BadRequestException("User not verified");
        }

        if (isResetTokenEmpty(user)) {
            throw new BadRequestException("Verification token is expired");
        }

        if (isResetTokenExpired(user)) {
            throw new BadRequestException("Invalid verification token");
        }
    }

    private boolean isNotEnabled(User user) {
        return !user.isEnabled();
    }

    private boolean isResetTokenEmpty(User user) {
        return user.getResetToken() == null;
    }

    private boolean isResetTokenExpired(User user) {
        return user.getResetTokenExpiresAt().isBefore(LocalDateTime.now());
    }

}
