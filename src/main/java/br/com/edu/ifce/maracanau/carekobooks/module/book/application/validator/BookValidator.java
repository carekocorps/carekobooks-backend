package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookExceedingGenreLimitException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookValidator implements BaseValidator<Book> {

    private final BookRepository bookRepository;

    public void validate(Book book) {
        if (isBookDuplicated(book)) {
            throw new BookConflictException();
        }

        if (isBookExceedingGenreLimit(book)) {
            throw new BookExceedingGenreLimitException();
        }
    }

    private boolean isBookDuplicated(Book book) {
        var query = new BookQuery();
        query.setTitle(book.getTitle());
        query.setAuthorName(book.getAuthorName());
        query.setPublisherName(book.getPublisherName());
        query.setPublishedBefore(book.getPublishedAt());
        query.setPublishedAfter(book.getPublishedAt());
        query.setPageCountLower(book.getPageCount());

        var books = bookRepository.findAll(query.getSpecification());
        return books.stream().anyMatch(b -> !b.getId().equals(book.getId()));
    }

    private boolean isBookExceedingGenreLimit(Book book) {
        return book.getGenres() != null && book.getGenres().size() > 5;
    }

}
