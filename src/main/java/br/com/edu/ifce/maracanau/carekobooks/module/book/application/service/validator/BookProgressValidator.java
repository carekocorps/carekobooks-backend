package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
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

        if (isPagesReadInvalid(bookProgress)) {
            throw new BadRequestException("Pages read cannot exceed total pages of the book");
        }
    }

    public boolean isUserEmpty(BookProgress bookProgress) {
        return bookProgress.getUser() == null;
    }

    public boolean isBookEmpty(BookProgress bookProgress) {
        return bookProgress.getBook() == null;
    }

    public boolean isPagesReadInvalid(BookProgress bookProgress) {
        return bookProgress.getPagesRead() != null && bookProgress.getPagesRead() > bookProgress.getBook().getTotalPages();
    }

}
