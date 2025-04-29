package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.progress.BookProgressNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.progress.BookProgressModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookProgressQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
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
        var response = bookProgressMapper.toResponse(bookProgressRepository.save(progress));

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(request.getUsername())) {
            throw new BookProgressModificationForbiddenException();
        }

        if (request.getScore() != null) {
            var userAverageScore = bookProgressRepository.calculateUserAverageScoreByBookId(request.getBookId());
            bookService.changeUserAverageScore(request.getBookId(), userAverageScore);
            response.getBook().setUserAverageScore(userAverageScore);
        }

        bookActivityService.create(request);
        return response;
    }

    @Transactional
    public BookProgressResponse update(Long id, BookProgressRequest request) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(BookProgressNotFoundException::new);

        bookProgressMapper.updateModel(progress, request);
        bookProgressValidator.validate(progress);
        bookProgressRepository.save(progress);

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(request.getUsername())) {
            throw new BookProgressModificationForbiddenException();
        }

        var userAverageScore = bookProgressRepository.calculateUserAverageScoreByBookId(request.getBookId());
        bookService.changeUserAverageScore(request.getBookId(), userAverageScore);

        bookActivityService.create(request);
        return bookProgressMapper.toResponse(progress);
    }

    @Transactional
    public void changeAsFavorite(Long id, boolean isFavorite) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(BookProgressNotFoundException::new);

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(progress.getUser().getUsername())) {
            throw new BookProgressModificationForbiddenException();
        }

        bookProgressRepository.changeAsFavoriteById(id, isFavorite);
    }

    @Transactional
    public void delete(Long id) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(BookProgressNotFoundException::new);

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(progress.getUser().getUsername())) {
            throw new BookProgressModificationForbiddenException();
        }

        bookProgressRepository.deleteById(id);
    }

}
