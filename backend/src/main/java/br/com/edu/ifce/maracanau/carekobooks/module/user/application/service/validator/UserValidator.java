package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.shared.exception.DuplicatedEntryException;
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
            throw new DuplicatedEntryException("A user with the same name already exists");
        }

        if (isEmailDuplicated(user)) {
            throw new DuplicatedEntryException("A user with the same email already exists");
        }
    }

    private boolean isUsernameDuplicated(User user) {
        return userRepository.existsByUsername(user.getUsername());
    }

    private boolean isEmailDuplicated(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }

}
