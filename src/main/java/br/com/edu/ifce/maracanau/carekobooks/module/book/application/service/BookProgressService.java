package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookProgressQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookProgressService {

    private final UserContextProvider userContextProvider;

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
        userContextProvider.assertAuthorized(request.getUsername(), BookProgressModificationForbiddenException.class);
        var progress = bookProgressMapper.toEntity(request);
        bookProgressValidator.validate(progress);
        progress = bookProgressRepository.save(progress);

        bookActivityService.create(request);
        return bookProgressMapper.toResponse(progress);
    }

    @Transactional
    public BookProgressResponse update(Long id, BookProgressRequest request) {
        userContextProvider.assertAuthorized(request.getUsername(), BookProgressModificationForbiddenException.class);
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(BookProgressNotFoundException::new);

        bookProgressMapper.updateEntity(progress, request);
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

        userContextProvider.assertAuthorized(progress.getUser().getUsername(), BookProgressModificationForbiddenException.class);
        bookProgressRepository.changeAsFavoriteById(id, isFavorite);
    }

    @Transactional
    public void delete(Long id) {
        var progress = bookProgressRepository
                .findById(id)
                .orElseThrow(BookProgressNotFoundException::new);

        userContextProvider.assertAuthorized(progress.getUser().getUsername(), BookProgressModificationForbiddenException.class);
        bookProgressRepository.delete(progress);
    }

}
