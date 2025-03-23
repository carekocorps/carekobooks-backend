package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.shared.AuthenticatedUser;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookProgressDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.query.BookProgressSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookProgressService {

    private final BookRepository bookRepository;

    private final BookProgressRepository bookProgressRepository;
    private final BookProgressMapper bookProgressMapper;
    private final BookProgressValidator bookProgressValidator;

    private final BookActivityRepository bookActivityRepository;
    private final BookActivityMapper bookActivityMapper;
    private final BookActivityValidator bookActivityValidator;

    public ApplicationPage<BookProgressDTO> search(BookProgressSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookProgressRepository.findAll(specification, pageRequest).map(bookProgressMapper::toDTO));
    }

    public Optional<BookProgressDTO> findById(Long id) {
        return bookProgressRepository.findById(id).map(bookProgressMapper::toDTO);
    }

    @Transactional
    public BookProgressDTO create(BookProgressRequest request) {
        if (!AuthenticatedUser.isAuthorOrAdmin(request.getUserId())) {
            throw new ForbiddenException("Forbidden");
        }

        var bookActivity = bookActivityMapper.toModel(request);
        bookActivityValidator.validate(bookActivity);
        bookActivityRepository.save(bookActivity);

        var bookProgress = bookProgressMapper.toModel(request);
        bookProgressValidator.validate(bookProgress);
        bookProgress = bookProgressRepository.save(bookProgress);

        if (bookRepository.existsById(request.getBookId()) && request.getScore() != null) {
            var averageScore = bookProgressRepository.findAverageScoreByBookId(request.getBookId());
            bookProgress.getBook().setAverageScore(averageScore);
            bookRepository.updateAverageScoreById(averageScore, request.getBookId());
        }

        return bookProgressMapper.toDTO(bookProgress);
    }

    @Transactional
    public void update(Long id, BookProgressRequest request) {
        if (!AuthenticatedUser.isAuthorOrAdmin(request.getUserId())) {
            throw new ForbiddenException("Forbidden");
        }

        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        bookProgressMapper.updateEntity(bookProgress, request);
        bookProgressValidator.validate(bookProgress);
        bookProgressRepository.save(bookProgress);

        if (bookRepository.existsById(request.getBookId()) && request.getScore() != null) {
            var averageScore = bookProgressRepository.findAverageScoreByBookId(request.getBookId());
            bookRepository.updateAverageScoreById(averageScore, request.getBookId());
        }
    }

    @Transactional
    public void updateIsMarkedAsFavoriteById(Boolean isMarkedAsFavorite, Long id) {
        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        if (!AuthenticatedUser.isAuthorOrAdmin(bookProgress.getUser().getId())) {
            throw new ForbiddenException("Forbidden");
        }

        bookProgressRepository.updateIsMarkedAsFavorite(isMarkedAsFavorite, id);
    }

    @Transactional
    public void deleteById(Long id) {
        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        if (!AuthenticatedUser.isAuthorOrAdmin(bookProgress.getUser().getId())) {
            throw new ForbiddenException("Forbidden");
        }

        bookProgressRepository.deleteById(id);
    }

}
