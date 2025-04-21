package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import org.springframework.stereotype.Component;

@Component
public class ForumValidator implements BaseValidator<Forum> {

    public void validate(Forum forum) {
        if (isUserEmpty(forum)) {
            throw new NotFoundException("User not found");
        }

        if (isBookEmpty(forum)) {
            throw new NotFoundException("Book not found");
        }
    }

    private boolean isUserEmpty(Forum forum) {
        return forum.getUser() == null;
    }

    private boolean isBookEmpty(Forum forum) {
        return forum.getBook() == null;
    }

}
