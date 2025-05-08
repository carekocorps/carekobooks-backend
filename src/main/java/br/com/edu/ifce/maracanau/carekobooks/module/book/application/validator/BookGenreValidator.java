package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNameConflictException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookGenreValidator implements BaseValidator<BookGenre> {

    private final BookGenreRepository bookGenreRepository;

    public void validate(BookGenre bookGenre) {
        if (isNameDuplicated(bookGenre)) {
            throw new BookGenreNameConflictException();
        }
    }

    private boolean isNameDuplicated(BookGenre bookGenre) {
        var genre = bookGenreRepository.findByName(bookGenre.getName());
        return genre.isPresent() && !genre.get().getId().equals(bookGenre.getId());
    }

}
