package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThreadReply;
import org.springframework.stereotype.Component;

@Component
public class BookThreadReplyValidator implements BaseValidator<BookThreadReply> {

    public void validate(BookThreadReply reply) {
        if (isUserEmpty(reply)) {
            throw new NotFoundException("User not found");
        }

        if (isThreadEmpty(reply)) {
            throw new NotFoundException("Thread not found");
        }
    }

    private boolean isUserEmpty(BookThreadReply reply) {
        return reply.getUser() == null;
    }

    private boolean isThreadEmpty(BookThreadReply reply) {
        return reply.getThread() == null;
    }

}
