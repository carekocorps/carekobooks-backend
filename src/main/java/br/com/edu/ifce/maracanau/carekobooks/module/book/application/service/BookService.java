package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookQuery;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookValidator;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ActionType;
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
    public BookResponse create(BookRequest request, MultipartFile image) throws Exception {
        var book = bookMapper.toModel(request);
        if (image != null) {
            book.setImage(imageMapper.toModel(imageService.create(image)));
        }

        bookValidator.validate(book);
        if (book.getGenres().size() != request.getGenres().size()) {
            throw new BadRequestException("Some genres are duplicated or invalid and could not be found");
        }

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @CachePut(value = "book", key = "#id")
    @Transactional
    public BookResponse update(Long id, BookRequest request, MultipartFile image) throws Exception {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        if (image != null) {
            book.setImage(imageMapper.toModel(imageService.create(image)));
        }

        bookMapper.updateModel(book, request);
        bookValidator.validate(book);
        if (book.getGenres().size() != request.getGenres().size()) {
            throw new BadRequestException("Some genres are duplicated or invalid and could not be found");
        }

        bookRepository.save(book);
        return bookMapper.toResponse(book);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeGenre(Long id, String genreName, ActionType action) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        var genre = bookGenreService
                .find(genreName)
                .map(bookGenreMapper::toModel)
                .orElseThrow(() -> new NotFoundException("Genre not found"));

        var isAdditionRequested = action == ActionType.ASSIGN;
        var isBookContainingGenre = book.getGenres().contains(genre);
        if (isBookContainingGenre == isAdditionRequested) {
            throw new BadRequestException(isAdditionRequested
                    ? "Book already contains this genre"
                    : "Book does not contain this genre"
            );
        }

        if (isAdditionRequested) bookRepository.addGenre(book.getId(), genre.getId());
        else bookRepository.removeGenre(book.getId(), genre.getId());
        bookValidator.validate(book);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeUserAverageScore(Long id, Double userAverageScore) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookRepository.changeUserAverageScoreById(id, userAverageScore);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeReviewAverageScore(Long id, Double reviewAverageScore) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookRepository.changeReviewAverageScoreById(id, reviewAverageScore);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void changeImage(Long id, MultipartFile image) throws Exception {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        if (image == null && book.getImage() != null) {
            imageService.delete(book.getImage().getId());
            book.setImage(null);
        } else {
            book.setImage(imageMapper.toModel(imageService.create(image)));
        }

        bookRepository.save(book);
    }

    @CacheEvict(value = "book", key = "#id")
    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookRepository.deleteById(id);
    }

    @CacheEvict(value = "book", allEntries = true)
    public void clearCache() {
    }

}
