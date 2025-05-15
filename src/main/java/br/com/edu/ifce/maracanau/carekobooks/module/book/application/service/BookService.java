package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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

    public ApplicationPage<BookResponse> search(BookQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookRepository.findAll(specification, pageRequest).map(bookMapper::toResponse));
    }

    @Cacheable(value = "book", key = "#id")
    public Optional<BookResponse> find(Long id) {
        return bookRepository.findById(id).map(bookMapper::toResponse);
    }

    @CacheEvict(value = "book", allEntries = true)
    @Transactional
    public BookResponse create(BookRequest request, MultipartFile image) {
        var book = bookMapper.toModel(request);
        if (image != null) {
            book.setImage(imageMapper.toModel(imageService.create(image)));
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

        if (book.getImage() != null) {
            imageService.delete(book.getImage().getId());
        }

        if (image != null) {
            book.setImage(imageMapper.toModel(imageService.create(image)));
        }

        bookMapper.updateModel(book, request);
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
                .map(bookGenreMapper::toModel)
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
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeUserAverageScore(Long id, Double userAverageScore) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException();
        }

        bookRepository.changeUserAverageScoreById(id, userAverageScore);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeReviewAverageScore(Long id, Double reviewAverageScore) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException();
        }

        bookRepository.changeReviewAverageScoreById(id, reviewAverageScore);
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
                .map(file -> imageMapper.toModel(imageService.create(file)))
                .orElse(null)
        );

        bookRepository.save(book);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException();
        }

        bookRepository.deleteById(id);
    }

    @CacheEvict(value = "book", allEntries = true)
    public void clearCache() { // @CacheEvict handles the cache clearing
    }

}
