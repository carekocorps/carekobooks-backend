package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import org.springframework.stereotype.Component;

@Component
public class BookActivityValidator implements BaseValidator<BookActivity> {

    public void validate(BookActivity activity) {
        if (isUserEmpty(activity)) {
            throw new UserNotFoundException();
        }

        if (isBookEmpty(activity)) {
            throw new BookNotFoundException();
        }
    }

    private boolean isUserEmpty(BookActivity activity) {
        return activity.getUser() == null;
    }

    private boolean isBookEmpty(BookActivity activity) {
        return activity.getBook() == null;
    }

}
