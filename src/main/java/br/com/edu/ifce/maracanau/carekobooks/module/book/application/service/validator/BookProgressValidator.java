package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.progress.BookProgressExceedingPageCountLimitException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import org.springframework.stereotype.Component;

@Component
public class BookProgressValidator implements BaseValidator<BookProgress> {

    public void validate(BookProgress bookProgress) {
        if (isUserEmpty(bookProgress)) {
            throw new UserNotFoundException();
        }

        if (isBookEmpty(bookProgress)) {
            throw new BookNotFoundException();
        }

        if (isPagesReadInvalid(bookProgress)) {
            throw new BookProgressExceedingPageCountLimitException();
        }
    }

    private boolean isUserEmpty(BookProgress bookProgress) {
        return bookProgress.getUser() == null;
    }

    private boolean isBookEmpty(BookProgress bookProgress) {
        return bookProgress.getBook() == null;
    }

    private boolean isPagesReadInvalid(BookProgress bookProgress) {
        return bookProgress.getPageCount() != null && bookProgress.getPageCount() > bookProgress.getBook().getPageCount();
    }

}
