package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookAlreadyContainingGenreException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotContainingGenreException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreCountMismatchException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    private final BookGenreService bookGenreService;
    private final BookGenreMapper bookGenreMapper;

    private final BookRepository bookRepository;
    private final BookValidator bookValidator;
    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    public ApplicationPage<SimplifiedBookResponse> search(BookQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookRepository.findAll(specification, pageRequest).map(bookMapper::toSimplifiedResponse));
    }

    @Cacheable(value = "book", key = "#id")
    @Transactional(readOnly = true)
    public Optional<BookResponse> find(Long id) {
        return bookRepository.findById(id).map(bookMapper::toResponse);
    }

    @CacheEvict(value = "book", allEntries = true)
    @Transactional
    public BookResponse create(BookRequest request, MultipartFile image) {
        var book = bookMapper.toEntity(request);
        if (image != null) {
            book.setImage(imageMapper.toEntity(imageService.create(image)));
        }

        bookValidator.validate(book);
        if (book.getGenres().size() != request.getGenres().size()) {
            throw new BookGenreCountMismatchException();
        }

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @CachePut(value = "book", key = "#id")
    @Transactional
    public BookResponse update(Long id, BookRequest request, MultipartFile image) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(BookNotFoundException::new);

        if (image != null) {
            book.setImage(imageMapper.toEntity(imageService.create(image)));
        }

        bookMapper.updateEntity(book, request);
        bookValidator.validate(book);
        if (book.getGenres().size() != request.getGenres().size()) {
            throw new BookGenreCountMismatchException();
        }

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeGenre(Long id, String genreName, boolean isAdditionRequested) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(BookNotFoundException::new);

        var genre = bookGenreService
                .find(genreName)
                .map(bookGenreMapper::toEntity)
                .orElseThrow(BookGenreNotFoundException::new);

        var isBookContainingGenre = book
                .getGenres()
                .stream()
                .anyMatch(bookGenre -> bookGenre.getId().equals(genre.getId()));

        if (isBookContainingGenre == isAdditionRequested) {
            throw isAdditionRequested
                    ? new BookAlreadyContainingGenreException()
                    : new BookNotContainingGenreException();
        }

        if (isAdditionRequested) bookRepository.addGenre(book.getId(), genre.getId());
        else bookRepository.removeGenre(book.getId(), genre.getId());
        bookValidator.validate(book);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeImage(Long id, MultipartFile image) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(BookNotFoundException::new);

        if (image == null && book.getImage() == null) {
            throw new ImageNotFoundException();
        }

        if (book.getImage() != null) {
            imageService.delete(book.getImage().getId());
        }

        book.setImage(Optional
                .ofNullable(image)
                .map(file -> imageMapper.toEntity(imageService.create(file)))
                .orElse(null)
        );

        bookRepository.save(book);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void delete(Long id) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(BookNotFoundException::new);

        bookRepository.delete(book);
    }

    @CacheEvict(value = "book", allEntries = true)
    public void clearCache() { // @CacheEvict handles the cache clearing
    }

}
