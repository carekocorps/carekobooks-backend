package br.com.edu.ifce.maracanau.carekobooks.validator;

import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookPageQueryDTO;
import br.com.edu.ifce.maracanau.carekobooks.exception.DuplicatedEntryException;
import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.repository.BookRepository;
import lombok.RequiredArgsConstructor;
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
        var bookSearch = new BookPageQueryDTO();
        bookSearch.setTitle(book.getTitle());
        bookSearch.setAuthor(book.getAuthor());
        bookSearch.setPublisher(book.getPublisher());
        bookSearch.setPublishedAt(book.getPublishedAt());
        bookSearch.setTotalPages(book.getTotalPages());

        var bookFound = bookRepository.findOne(bookSearch.getSpecification());
        return bookFound.isPresent() && !bookFound.get().getId().equals(book.getId());
    }

}
