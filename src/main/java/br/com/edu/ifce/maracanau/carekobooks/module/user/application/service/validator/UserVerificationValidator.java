package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserVerificationValidator implements BaseValidator<User> {

    public void validate(User user) {
        if (isEnabled(user)) {
            throw new BadRequestException("User is already verified");
        }

        if (isVerificationTokenExpired(user)) {
            throw new BadRequestException("Verification token is expired");
        }
    }

    private boolean isEnabled(User user) {
        return user.isEnabled();
    }

    private boolean isVerificationTokenExpired(User user) {
        return user.getVerificationTokenExpiresAt().isBefore(LocalDateTime.now());
    }

}
