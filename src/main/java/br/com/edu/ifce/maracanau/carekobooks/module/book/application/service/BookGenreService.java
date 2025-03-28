package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookGenreDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookGenreValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookGenreService {

    private final BookGenreRepository bookGenreRepository;
    private final BookGenreValidator bookGenreValidator;
    private final BookGenreMapper bookGenreMapper;

    public Optional<BookGenreDTO> findById(String name) {
        return bookGenreRepository.findByName(name).map(bookGenreMapper::toDTO);
    }

    @Transactional
    public BookGenreDTO create(BookGenreRequest request) {
        var book = bookGenreMapper.toModel(request);
        bookGenreValidator.validate(book);
        return bookGenreMapper.toDTO(bookGenreRepository.save(book));
    }

    @Transactional
    public void update(String name, BookGenreRequest request) {
        var book = bookGenreRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        bookGenreMapper.updateEntity(book, request);
        bookGenreValidator.validate(book);
        bookGenreRepository.save(book);
    }

    @Transactional
    public void deleteByName(String name) {
        if (!bookGenreRepository.existsByName(name)) {
            throw new NotFoundException("Book not found");
        }

        bookGenreRepository.deleteByName(name);
    }

}
