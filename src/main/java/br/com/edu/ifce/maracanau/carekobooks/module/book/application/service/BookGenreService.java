package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookGenreQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookGenreValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookGenreService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BookGenreRepository bookGenreRepository;
    private final BookGenreValidator bookGenreValidator;
    private final BookGenreMapper bookGenreMapper;

    @Transactional(readOnly = true)
    public ApplicationPage<BookGenreResponse> search(BookGenreQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookGenreRepository.findAll(specification, pageRequest).map(bookGenreMapper::toResponse));
    }

    @Cacheable(value = "book:genre", key = "#name")
    @Transactional(readOnly = true)
    public Optional<BookGenreResponse> find(String name) {
        return bookGenreRepository.findByName(name).map(bookGenreMapper::toResponse);
    }

    @CacheEvict(value = {"book", "book:genre"}, allEntries = true)
    @Transactional
    public BookGenreResponse create(BookGenreRequest request) {
        var genre = bookGenreMapper.toEntity(request);
        bookGenreValidator.validate(genre);
        return bookGenreMapper.toResponse(bookGenreRepository.save(genre));
    }

    @Caching(
            put = @CachePut(value = "book:genre", key = "#name"),
            evict = @CacheEvict(value = "book", allEntries = true)
    )
    @Transactional
    public BookGenreResponse update(String name, BookGenreRequest request) {
        var genre = bookGenreRepository
                .findByName(name)
                .orElseThrow(BookGenreNotFoundException::new);

        entityManager.detach(genre);
        bookGenreMapper.updateEntity(genre, request);
        bookGenreValidator.validate(genre);
        return bookGenreMapper.toResponse(bookGenreRepository.save(entityManager.merge(genre)));
    }

    @Caching(evict = {
            @CacheEvict(value = "book:genre", key = "#name"),
            @CacheEvict(value = "book", allEntries = true)
    })
    @Transactional
    public void delete(String name) {
        var genre = bookGenreRepository
                .findByName(name)
                .orElseThrow(BookGenreNotFoundException::new);

        bookGenreRepository.delete(genre);
    }

    @CacheEvict(
            value = {
                    "books",
                    "book:genre"
            },
            allEntries = true
    )
    public void clearCache() { // @CacheEvict handles the cache clearing
    }

}
