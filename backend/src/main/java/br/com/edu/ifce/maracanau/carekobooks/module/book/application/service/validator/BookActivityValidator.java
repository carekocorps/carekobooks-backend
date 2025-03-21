package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookActivityValidator {

    private final BookActivityRepository bookActivityRepository;

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
