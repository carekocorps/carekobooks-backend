package br.com.edu.ifce.maracanau.carekobooks.service;

import br.com.edu.ifce.maracanau.carekobooks.core.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookPageQueryDTO;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.validator.BookValidator;
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

    public ApplicationPage<BookDTO> search(BookPageQueryDTO bookSearchDTO) {
        var specification = bookSearchDTO.getSpecification();
        var sort = bookSearchDTO.getSort();
        var pageRequest = PageRequest.of(bookSearchDTO.getPageNumber(), bookSearchDTO.getPageSize(), sort);
        return new ApplicationPage<>(bookRepository.findAll(specification, pageRequest).map(bookMapper::toDTO));
    }

    public Optional<BookDTO> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDTO);
    }

    public BookDTO create(BookRequestDTO bookRequestDTO) {
        var book = bookMapper.toEntity(bookRequestDTO);
        bookValidator.validate(book);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    public void update(Long id, BookRequestDTO bookRequestDTO) {
        var book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        bookMapper.updateEntity(book, bookRequestDTO);
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
