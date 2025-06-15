package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import org.springframework.stereotype.Component;

@Component
public class BookThreadValidator implements BaseValidator<BookThread> {

    public void validate(BookThread thread) {
        if (isUserEmpty(thread)) {
            throw new UserNotFoundException();
        }

        if (isBookEmpty(thread)) {
            throw new BookNotFoundException();
        }
    }

    private boolean isUserEmpty(BookThread thread) {
        return thread.getUser() == null;
    }

    private boolean isBookEmpty(BookThread thread) {
        return thread.getBook() == null;
    }

}
