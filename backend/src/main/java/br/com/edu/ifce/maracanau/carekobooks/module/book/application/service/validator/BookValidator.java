package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.exception.ConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookValidator {

    private final BookRepository bookRepository;

    public void validate(Book book) {
        if (isBookDuplicated(book)) {
            throw new ConflictException("A book with the same information already exists");
        }
    }

    private boolean isBookDuplicated(Book book) {
        var query = new BookSearchQuery();
        query.setTitle(book.getTitle());
        query.setAuthor(book.getAuthor());
        query.setPublisher(book.getPublisher());
        query.setPublishedBefore(book.getPublishedAt());
        query.setPublishedAfter(book.getPublishedAt());
        query.setTotalPages(book.getTotalPages());

        var bookFound = bookRepository.findOne(query.getSpecification());
        return bookFound.isPresent() && !bookFound.get().getId().equals(book.getId());
    }

}
