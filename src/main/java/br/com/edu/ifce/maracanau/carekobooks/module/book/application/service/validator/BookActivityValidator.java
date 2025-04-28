package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import org.springframework.stereotype.Component;

@Component
public class BookActivityValidator implements BaseValidator<BookActivity> {

    public void validate(BookActivity bookActivity) {
        if (isUserEmpty(bookActivity)) {
            throw new UserNotFoundException();
        }

        if (isBookEmpty(bookActivity)) {
            throw new BookNotFoundException();
        }
    }

    private boolean isUserEmpty(BookActivity bookActivity) {
        return bookActivity.getUser() == null;
    }

    private boolean isBookEmpty(BookActivity bookActivity) {
        return bookActivity.getBook() == null;
    }

}
