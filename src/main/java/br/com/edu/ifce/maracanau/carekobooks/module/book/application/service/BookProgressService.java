package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookProgressDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookProgressSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
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
    private final BookProgressValidator bookProgressValidator;
    private final BookProgressMapper bookProgressMapper;

    private final BookActivityRepository bookActivityRepository;
    private final BookActivityValidator bookActivityValidator;
    private final BookActivityMapper bookActivityMapper;

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
        var bookProgress = bookProgressMapper.toModel(request);
        bookProgressValidator.validate(bookProgress);
        bookProgress = bookProgressRepository.save(bookProgress);

        if (!UserContextProvider.isCurrentUserAuthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to create this book progress");
        }

        if (bookRepository.existsById(request.getBookId()) && request.getScore() != null) {
            var userAverageScore = bookProgressRepository.findUserAverageScoreByBookId(request.getBookId());
            bookProgress.getBook().setUserAverageScore(userAverageScore);
            bookRepository.updateUserAverageScoreById(userAverageScore, request.getBookId());
        }

        var bookActivity = bookActivityMapper.toModel(request);
        bookActivityValidator.validate(bookActivity);
        bookActivityRepository.save(bookActivity);
        return bookProgressMapper.toDTO(bookProgress);
    }

    @Transactional
    public void update(Long id, BookProgressRequest request) {
        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        bookProgressMapper.updateEntity(bookProgress, request);
        bookProgressValidator.validate(bookProgress);
        bookProgressRepository.save(bookProgress);

        if (bookRepository.existsById(request.getBookId())) {
            var userAverageScore = bookProgressRepository.findUserAverageScoreByBookId(request.getBookId());
            bookRepository.updateUserAverageScoreById(userAverageScore, request.getBookId());
        }

        var bookActivity = bookActivityMapper.toModel(request);
        bookActivityValidator.validate(bookActivity);
        bookActivityRepository.save(bookActivity);
    }

    @Transactional
    public void updateIsMarkedAsFavoriteById(Boolean isMarkedAsFavorite, Long id) {
        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        bookProgressRepository.updateIsMarkedAsFavorite(isMarkedAsFavorite, id);
    }

    @Transactional
    public void deleteById(Long id) {
        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this book progress");
        }

        bookProgressRepository.deleteById(id);
    }

}
