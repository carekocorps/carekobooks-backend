package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookProgressQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressExceedingPageCountLimitException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressUserConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookProgressValidator implements BaseValidator<BookProgress> {

    private final BookProgressRepository bookProgressRepository;

    public void validate(BookProgress progress) {
        if (isUserEmpty(progress)) {
            throw new UserNotFoundException();
        }

        if (isBookEmpty(progress)) {
            throw new BookNotFoundException();
        }

        if (isPagesReadInvalid(progress)) {
            throw new BookProgressExceedingPageCountLimitException();
        }

        if (isUserProgressDuplicated(progress)) {
            throw new BookProgressUserConflictException();
        }
    }

    private boolean isUserEmpty(BookProgress progress) {
        return progress.getUser() == null;
    }

    private boolean isBookEmpty(BookProgress progress) {
        return progress.getBook() == null;
    }

    private boolean isPagesReadInvalid(BookProgress progress) {
        return progress.getPageCount() != null && progress.getPageCount() > progress.getBook().getPageCount();
    }

    private boolean isUserProgressDuplicated(BookProgress progress) {
        var query = new BookProgressQuery();
        query.setUsername(progress.getUser().getUsername());
        query.setBookId(progress.getBook().getId());

        var progresses = bookProgressRepository.findAll(query.getSpecification());
        return !progresses.isEmpty();
    }

}
