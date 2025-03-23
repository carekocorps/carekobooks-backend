package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.shared.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import org.springframework.stereotype.Component;

@Component
public class BookActivityValidator {

    public void validate(BookActivity bookActivity) {
        if (isUserEmpty(bookActivity)) {
            throw new NotFoundException("User not found");
        }

        if (isBookEmpty(bookActivity)) {
            throw new NotFoundException("Book not found");
        }
    }

    public boolean isUserEmpty(BookActivity bookActivity) {
        return bookActivity.getUser() == null;
    }

    public boolean isBookEmpty(BookActivity bookActivity) {
        return bookActivity.getBook() == null;
    }

}
