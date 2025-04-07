package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookValidator;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ToggleAction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    public void updateGenreById(Long id, String genreName, ToggleAction action) {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        var bookGenre = bookGenreService
                .findByName(genreName)
                .map(bookGenreMapper::toModel)
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
    public void updateUserAverageScoreById(Long id, Double userAverageScore) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookRepository.updateUserAverageScoreById(userAverageScore, id);
    }

    @Transactional
    public void updateReviewAverageScoreById(Long id, Double reviewAverageScore) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookRepository.updateReviewAverageScoreById(reviewAverageScore, id);
    }

    @Transactional
    public void updateImageById(Long id, MultipartFile image) throws Exception {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        book.setImage(imageMapper.toModel(imageService.create(image)));
        bookRepository.save(book);
    }

    @Transactional
    public void deleteImageById(Long id) throws Exception {
        var book = bookRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        if (book.getImage() == null) {
            throw new NotFoundException("Image not found or already deleted");
        }

        imageService.deleteById(book.getImage().getId());
        book.setImage(null);
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
