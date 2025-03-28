package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.exception.ConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookGenreValidator {

    private final BookGenreRepository bookGenreRepository;

    public void validate(BookGenre bookGenre) {
        if (isNameDuplicated(bookGenre)) {
            throw new ConflictException("Book Genre with the same name already exists");
        }
    }

    private boolean isNameDuplicated(BookGenre bookGenre) {
        var genre = bookGenreRepository.findByName(bookGenre.getName());
        return genre.isPresent() && !genre.get().getId().equals(bookGenre.getId());
    }

}
