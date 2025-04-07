package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookProgressSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ToggleAction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookProgressService {

    private final BookService bookService;
    private final BookActivityService bookActivityService;

    private final BookProgressRepository bookProgressRepository;
    private final BookProgressValidator bookProgressValidator;
    private final BookProgressMapper bookProgressMapper;

    public ApplicationPage<BookProgressResponse> search(BookProgressSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookProgressRepository.findAll(specification, pageRequest).map(bookProgressMapper::toResponse));
    }

    public Optional<BookProgressResponse> findById(Long id) {
        return bookProgressRepository.findById(id).map(bookProgressMapper::toResponse);
    }

    @Transactional
    public BookProgressResponse create(BookProgressRequest request) {
        var bookProgress = bookProgressMapper.toModel(request);
        bookProgressValidator.validate(bookProgress);
        bookProgress = bookProgressRepository.save(bookProgress);

        if (!UserContextProvider.isCurrentUserAuthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to create this book progress");
        }

        if (request.getScore() != null) {
            var userAverageScore = bookProgressRepository.findUserAverageScoreByBookId(request.getBookId());
            bookService.updateUserAverageScoreById(request.getBookId(), userAverageScore);
            bookProgress.getBook().setUserAverageScore(userAverageScore);
        }

        bookActivityService.create(request);
        return bookProgressMapper.toResponse(bookProgress);
    }

    @Transactional
    public void update(Long id, BookProgressRequest request) {
        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        bookProgressMapper.updateModel(bookProgress, request);
        bookProgressValidator.validate(bookProgress);
        bookProgressRepository.save(bookProgress);

        if (!UserContextProvider.isCurrentUserAuthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        var userAverageScore = bookProgressRepository.findUserAverageScoreByBookId(request.getBookId());
        bookService.updateUserAverageScoreById(request.getBookId(), userAverageScore);
        bookProgress.getBook().setUserAverageScore(userAverageScore);

        bookActivityService.create(request);
    }

    @Transactional
    public void updateIsFavoriteById(Long id, ToggleAction action) {
        var bookProgress = bookProgressRepository.findById(id).orElse(null);
        if (bookProgress == null) {
            throw new NotFoundException("Book Progress not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        var isAssignRequested = action == ToggleAction.ASSIGN;
        bookProgressRepository.updateIsFavoriteById(isAssignRequested, id);
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
