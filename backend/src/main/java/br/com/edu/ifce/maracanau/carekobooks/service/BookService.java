package br.com.edu.ifce.maracanau.carekobooks.service;

import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.util.page.CustomPage;
import br.com.edu.ifce.maracanau.carekobooks.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookValidator bookValidator;
    private final BookMapper bookMapper;

    public CustomPage<BookDTO> search(BookRequestDTO bookRequestDTO) {

    }

}
