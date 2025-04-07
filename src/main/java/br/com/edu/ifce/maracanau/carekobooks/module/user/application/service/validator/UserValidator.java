package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.ConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    public void validate(User user) {
        if (isUsernameDuplicated(user)) {
            throw new ConflictException("A user with the same name already exists");
        }

        if (isEmailDuplicated(user)) {
            throw new ConflictException("A user with the same email already exists");
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
