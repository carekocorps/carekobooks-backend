package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import org.springframework.stereotype.Component;

@Component
public class ForumValidator {

    public void validate(Forum forum) {
        if (isUserEmpty(forum)) {
            throw new NotFoundException("User not found");
        }

        if (isBookEmpty(forum)) {
            throw new NotFoundException("Book not found");
        }
    }

    public boolean isUserEmpty(Forum forum) {
        return forum.getUser() == null;
    }

    public boolean isBookEmpty(Forum forum) {
        return forum.getBook() == null;
    }

}
