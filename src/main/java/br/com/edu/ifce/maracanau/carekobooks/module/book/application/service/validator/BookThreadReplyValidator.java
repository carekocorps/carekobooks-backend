package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThreadReply;
import org.springframework.stereotype.Component;

@Component
public class BookThreadReplyValidator implements BaseValidator<BookThreadReply> {

    public void validate(BookThreadReply reply) {
        if (isUserEmpty(reply)) {
            throw new UserNotFoundException();
        }

        if (isThreadEmpty(reply)) {
            throw new BookThreadNotFoundException();
        }
    }

    private boolean isUserEmpty(BookThreadReply reply) {
        return reply.getUser() == null;
    }

    private boolean isThreadEmpty(BookThreadReply reply) {
        return reply.getThread() == null;
    }

}
