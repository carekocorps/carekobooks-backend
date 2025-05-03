package br.com.edu.ifce.maracanau.carekobooks.module.user.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserChangeUsernameValidator implements BaseValidator<User> {

    @Override
    public void validate(User user) {
        if (isNotEnabled(user)) {
            throw new UserNotVerifiedException();
        }
    }

    private boolean isNotEnabled(User user) {
        return !user.isEnabled();
    }

}
