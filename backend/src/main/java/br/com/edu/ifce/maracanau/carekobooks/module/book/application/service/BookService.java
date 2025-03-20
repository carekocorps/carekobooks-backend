package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.query.BookSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookValidator bookValidator;
    private final BookMapper bookMapper;

    public ApplicationPage<BookDTO> search(BookSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookRepository.findAll(specification, pageRequest).map(bookMapper::toDTO));
    }

    public Optional<BookDTO> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDTO);
    }

    public BookDTO create(BookRequestDTO request) {
        var book = bookMapper.toEntity(request);
        bookValidator.validate(book);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    public void update(Long id, BookRequestDTO request) {
        var book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        bookMapper.updateEntity(book, request);
        bookValidator.validate(book);
        bookRepository.save(book);
    }

    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookRepository.deleteById(id);
    }

}
