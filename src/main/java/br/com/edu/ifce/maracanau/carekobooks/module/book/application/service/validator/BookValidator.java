package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ConflictException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookValidator implements BaseValidator<Book> {

    private final BookRepository bookRepository;

    public void validate(Book book) {
        if (isBookDuplicated(book)) {
            throw new ConflictException("A book with the same information already exists");
        }

        if (isBookExceedingGenreLimit(book)) {
            throw new BadRequestException("A book can have at most 5 genres");
        }
    }

    private boolean isBookDuplicated(Book book) {
        var query = new BookQuery();
        query.setTitle(book.getTitle());
        query.setAuthorName(book.getAuthorName());
        query.setPublisherName(book.getPublisherName());
        query.setPublishedBefore(book.getPublishedAt());
        query.setPublishedAfter(book.getPublishedAt());
        query.setPageCount(book.getPageCount());

        var books = bookRepository.findAll(query.getSpecification());
        return books.stream().anyMatch(b -> !b.getId().equals(book.getId()));
    }

    private boolean isBookExceedingGenreLimit(Book book) {
        return book.getGenres() != null && book.getGenres().size() > 5;
    }

}
