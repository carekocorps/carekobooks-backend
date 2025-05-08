package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
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
