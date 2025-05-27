package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookProgressQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.annotation.AuthenticatedUserMatchRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookProgressService {

    private final BookActivityService bookActivityService;

    private final BookProgressRepository bookProgressRepository;
    private final BookProgressValidator bookProgressValidator;
    private final BookProgressMapper bookProgressMapper;

    @Transactional(readOnly = true)
    public ApplicationPage<BookProgressResponse> search(BookProgressQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookProgressRepository.findAll(specification, pageRequest).map(bookProgressMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public Optional<BookProgressResponse> find(Long id) {
        return bookProgressRepository.findById(id).map(bookProgressMapper::toResponse);
    }

    @Transactional
    public BookProgressResponse create(BookProgressRequest request) {
        var progress = bookProgressMapper.toModel(request);
        bookProgressValidator.validate(progress);
        progress = bookProgressRepository.save(progress);

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(request.getUsername())) {
            throw new BookProgressModificationForbiddenException();
        }

        bookActivityService.create(request);
        return bookProgressMapper.toResponse(progress);
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "request", exception = BookProgressNotFoundException.class)
    public BookProgressResponse update(Long id, BookProgressRequest request) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(BookProgressNotFoundException::new);

        bookProgressMapper.updateModel(progress, request);
        bookProgressValidator.validate(progress);
        progress = bookProgressRepository.save(progress);

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
