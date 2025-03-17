package br.com.edu.ifce.maracanau.carekobooks.validator;

import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookValidator {

    private final BookRepository bookRepository;

    public void validate(Book book) {

    }

}
