package br.com.edu.ifce.maracanau.carekobooks.validator;

import br.com.edu.ifce.maracanau.carekobooks.exception.DuplicatedEntryException;
import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookValidator {

    private final BookRepository bookRepository;

    public void validate(Book book) {
        if (isBookDuplicated(book)) {
            throw new DuplicatedEntryException("A book with the same information already exists");
        }
    }

    private boolean isBookDuplicated(Book book) {
        var bookExample = new Book();
        bookExample.setTitle(book.getTitle());
        bookExample.setAuthor(book.getAuthor());
        bookExample.setPublisher(book.getPublisher());
        bookExample.setPublishedAt(book.getPublishedAt());
        bookExample.setTotalPages(book.getTotalPages());

        var bookFound = bookRepository.findOne(Example.of(bookExample));
        return bookFound.isPresent() && !bookFound.get().getId().equals(book.getId());
    }

}
