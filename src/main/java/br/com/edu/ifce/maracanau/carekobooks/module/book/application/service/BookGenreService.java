package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookGenreSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookGenreValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookGenreService {

    private final BookGenreRepository bookGenreRepository;
    private final BookGenreValidator bookGenreValidator;
    private final BookGenreMapper bookGenreMapper;

    public ApplicationPage<BookGenreResponse> search(BookGenreSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookGenreRepository.findAll(specification, pageRequest).map(bookGenreMapper::toResponse));
    }

    @Cacheable(value = "book_genres", key = "#name")
    public Optional<BookGenreResponse> findByName(String name) {
        return bookGenreRepository.findByName(name).map(bookGenreMapper::toResponse);
    }

    @CacheEvict(value = {"books", "book_genres"}, allEntries = true)
    @Transactional
    public BookGenreResponse create(BookGenreRequest request) {
        var bookGenre = bookGenreMapper.toModel(request);
        bookGenreValidator.validate(bookGenre);
        return bookGenreMapper.toResponse(bookGenreRepository.save(bookGenre));
    }

    @Caching(
            put = @CachePut(value = "book_genres", key = "#name"),
            evict = @CacheEvict(value = "books", allEntries = true)
    )
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

    @Caching(evict = {
            @CacheEvict(value = "book_genres", key = "#name"),
            @CacheEvict(value = "books", allEntries = true)
    })
    @Transactional
    public void deleteByName(String name) {
        if (!bookGenreRepository.existsByName(name)) {
            throw new NotFoundException("Book not found");
        }

        bookGenreRepository.deleteByName(name);
    }

    @CacheEvict(value = {"books", "book_genres"}, allEntries = true)
    public void clearCache() {
    }

}
