package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.shared.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import org.springframework.stereotype.Component;

@Component
public class BookProgressValidator {

    public void validate(BookProgress bookProgress) {
        if (isUserEmpty(bookProgress)) {
            throw new NotFoundException("User not found");
        }

        if (isBookEmpty(bookProgress)) {
            throw new NotFoundException("Book not found");
        }
    }

    public boolean isUserEmpty(BookProgress bookProgress) {
        return bookProgress.getUser() == null;
    }

    public boolean isBookEmpty(BookProgress bookProgress) {
        return bookProgress.getBook() == null;
    }

}
