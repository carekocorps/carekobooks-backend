package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThread;
import org.springframework.stereotype.Component;

@Component
public class BookThreadValidator implements BaseValidator<BookThread> {

    public void validate(BookThread bookThread) {
        if (isUserEmpty(bookThread)) {
            throw new UserNotFoundException();
        }

        if (isBookEmpty(bookThread)) {
            throw new BookNotFoundException();
        }
    }

    private boolean isUserEmpty(BookThread bookThread) {
        return bookThread.getUser() == null;
    }

    private boolean isBookEmpty(BookThread bookThread) {
        return bookThread.getBook() == null;
    }

}
