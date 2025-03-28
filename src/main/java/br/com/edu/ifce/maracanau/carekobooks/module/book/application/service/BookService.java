package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookValidator;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.service.enums.ToggleAction;
import jakarta.transaction.Transactional;
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
    private final BookGenreRepository bookGenreRepository;

    public ApplicationPage<BookDTO> search(BookSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookRepository.findAll(specification, pageRequest).map(bookMapper::toDTO));
    }

    public Optional<BookDTO> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDTO);
    }

    @Transactional
    public BookDTO create(BookRequest request) {
        var book = bookMapper.toModel(request);
        bookValidator.validate(book);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Transactional
    public void update(Long id, BookRequest request) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        bookMapper.updateEntity(book, request);
        bookValidator.validate(book);
        bookRepository.save(book);
    }

    @Transactional
    public void updateBookGenresById(Long id, String genreName, ToggleAction action) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        var bookGenre = bookGenreRepository
                .findByName(genreName)
                .orElseThrow(() -> new NotFoundException("Genre not found"));

        var isAssignRequested = action == ToggleAction.ASSIGN;
        var isBookContainingGenre = book.getGenres().contains(bookGenre);
        if (isBookContainingGenre == isAssignRequested) {
            throw new BadRequestException(isAssignRequested
                    ? "Book already contains this genre"
                    : "Book does not contain this genre"
            );
        }

        if (isAssignRequested) book.getGenres().add(bookGenre);
        else book.getGenres().remove(bookGenre);
        bookRepository.save(book);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookRepository.deleteById(id);
    }

}
