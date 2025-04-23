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
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ActionType;
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
        var progress = bookProgressMapper.toModel(request);
        bookProgressValidator.validate(progress);
        progress = bookProgressRepository.save(progress);

        if (UserContextProvider.isUserUnauthorized(progress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to create this book progress");
        }

        if (request.getScore() != null) {
            var userAverageScore = bookProgressRepository.calculateUserAverageScoreByBookId(request.getBookId());
            bookService.changeUserAverageScore(request.getBookId(), userAverageScore);
        }

        bookActivityService.create(request);
        return bookProgressMapper.toResponse(progress);
    }

    @Transactional
    public BookProgressResponse update(Long id, BookProgressRequest request) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Progress not found"));

        bookProgressMapper.updateModel(progress, request);
        bookProgressValidator.validate(progress);
        bookProgressRepository.save(progress);

        if (UserContextProvider.isUserUnauthorized(progress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        var userAverageScore = bookProgressRepository.calculateUserAverageScoreByBookId(request.getBookId());
        bookService.changeUserAverageScore(request.getBookId(), userAverageScore);

        bookActivityService.create(request);
        return bookProgressMapper.toResponse(progress);
    }

    @Transactional
    public void changeAsFavorite(Long id, ActionType action) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Progress not found"));

        if (UserContextProvider.isUserUnauthorized(progress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this book progress");
        }

        var isFavorite = action == ActionType.ASSIGN;
        bookProgressRepository.changeAsFavoriteById(id, isFavorite);
    }

    @Transactional
    public void delete(Long id) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Progress not found"));

        if (UserContextProvider.isUserUnauthorized(progress.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this book progress");
        }

        bookProgressRepository.deleteById(id);
    }

}
