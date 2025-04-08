package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookGenreValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookGenreService {

    private final BookGenreRepository bookGenreRepository;
    private final BookGenreValidator bookGenreValidator;
    private final BookGenreMapper bookGenreMapper;

    @Cacheable("book_genres")
    public Optional<BookGenreResponse> findByName(String name) {
        return bookGenreRepository.findByName(name).map(bookGenreMapper::toResponse);
    }

    @CacheEvict(value = "book_genres", allEntries = true)
    @Transactional
    public BookGenreResponse create(BookGenreRequest request) {
        var bookGenre = bookGenreMapper.toModel(request);
        bookGenreValidator.validate(bookGenre);
        return bookGenreMapper.toResponse(bookGenreRepository.save(bookGenre));
    }

    @CachePut(value = "book_genres", key = "#name")
    @Transactional
    public BookGenreResponse update(String name, BookGenreRequest request) {
        var bookGenre = bookGenreRepository
                .findByName(name)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        bookGenreMapper.updateModel(bookGenre, request);
        bookGenreValidator.validate(bookGenre);
        bookGenreRepository.save(bookGenre);
        return bookGenreMapper.toResponse(bookGenre);
    }

    @CacheEvict(value = "book_genres", key = "#name")
    @Transactional
    public void deleteByName(String name) {
        if (!bookGenreRepository.existsByName(name)) {
            throw new NotFoundException("Book not found");
        }

        bookGenreRepository.deleteByName(name);
    }

    @CacheEvict(value = "book_genres", allEntries = true)
    public void clearCache() {
    }

}
