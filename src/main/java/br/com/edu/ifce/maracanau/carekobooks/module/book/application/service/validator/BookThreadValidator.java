package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThread;
import org.springframework.stereotype.Component;

@Component
public class BookThreadValidator implements BaseValidator<BookThread> {

    public void validate(BookThread bookThread) {
        if (isUserEmpty(bookThread)) {
            throw new NotFoundException("User not found");
        }

        if (isBookEmpty(bookThread)) {
            throw new NotFoundException("Book not found");
        }
    }

    private boolean isUserEmpty(BookThread bookThread) {
        return bookThread.getUser() == null;
    }

    private boolean isBookEmpty(BookThread bookThread) {
        return bookThread.getBook() == null;
    }

}
