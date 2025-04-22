package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookProgressQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.IntentType;
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

    public ApplicationPage<BookProgressResponse> search(BookProgressQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookProgressRepository.findAll(specification, pageRequest).map(bookProgressMapper::toResponse));
    }

    public Optional<BookProgressResponse> find(Long id) {
        return bookProgressRepository.findById(id).map(bookProgressMapper::toResponse);
    }

    @Transactional
    public BookProgressResponse create(BookProgressRequest request) {
        var bookProgress = bookProgressMapper.toModel(request);
        bookProgressValidator.validate(bookProgress);
        var response = bookProgressMapper.toResponse(bookProgressRepository.save(bookProgress));

        if (UserContextProvider.isUserUnauthorized(response.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to create this book progress");
        }

        if (request.getScore() != null) {
            var userAverageScore = bookProgressRepository.findUserAverageScoreByBookId(request.getBookId());
            bookService.updateUserAverageScoreById(request.getBookId(), userAverageScore);
            response.getBook().setUserAverageScore(userAverageScore);
        }

        bookActivityService.create(request);
        return response;
    }

    @Transactional
    public BookProgressResponse update(Long id, BookProgressRequest request) {
        var bookProgress = bookProgressRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Progress not found"));

        bookProgressMapper.updateModel(bookProgress, request);
        bookProgressValidator.validate(bookProgress);
        var response = bookProgressMapper.toResponse(bookProgressRepository.save(bookProgress));

        if (UserContextProvider.isUserUnauthorized(response.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        var userAverageScore = bookProgressRepository.findUserAverageScoreByBookId(request.getBookId());
        bookService.updateUserAverageScoreById(request.getBookId(), userAverageScore);
        response.getBook().setUserAverageScore(userAverageScore);

        bookActivityService.create(request);
        return response;
    }

    @Transactional
    public void changeAsFavorite(Long id, IntentType action) {
        var bookProgress = bookProgressRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Progress not found"));

        if (UserContextProvider.isUserUnauthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        var isAssignRequested = action == IntentType.ASSIGN;
        bookProgressRepository.updateIsFavoriteById(isAssignRequested, id);
    }

    @Transactional
    public void delete(Long id) {
        var bookProgress = bookProgressRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Progress not found"));

        if (UserContextProvider.isUserUnauthorized(bookProgress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this book progress");
        }

        bookProgressRepository.deleteById(id);
    }

}
