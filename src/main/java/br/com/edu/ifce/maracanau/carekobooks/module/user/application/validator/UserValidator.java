package br.com.edu.ifce.maracanau.carekobooks.module.user.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserEmailConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserUsernameConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator implements BaseValidator<User> {

    private final UserRepository userRepository;

    public void validate(User user) {
        if (isUsernameDuplicated(user)) {
            throw new UserUsernameConflictException();
        }

        if (isEmailDuplicated(user)) {
            throw new UserEmailConflictException();
        }
    }

    private boolean isUsernameDuplicated(User user) {
        var existingUser = userRepository.findByUsername(user.getUsername());
        return existingUser.isPresent() && !existingUser.get().getId().equals(user.getId());
    }

    private boolean isEmailDuplicated(User user) {
        var existingUser = userRepository.findByEmail(user.getEmail());
        return existingUser.isPresent() && !existingUser.get().getId().equals(user.getId());
    }

}
