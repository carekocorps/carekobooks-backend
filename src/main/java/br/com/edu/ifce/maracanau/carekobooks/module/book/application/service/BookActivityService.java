package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookActivityFeedQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.activity.BookActivityModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.activity.BookActivityNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.subject.BookActivityNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookActivityQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookActivityService {

    private final BookActivityRepository bookActivityRepository;
    private final BookActivityMapper bookActivityMapper;
    private final BookActivityValidator bookActivityValidator;
    private final BookActivityNotificationSubject bookActivityNotificationSubject;

    @Transactional(readOnly = true)
    public ApplicationPage<BookActivityResponse> search(BookActivityQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookActivityRepository.findAll(specification, pageRequest).map(bookActivityMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public ApplicationPage<BookActivityResponse> search(BookActivityFeedQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookActivityRepository.findAll(specification, pageRequest).map(bookActivityMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public Optional<BookActivityResponse> find(Long id) {
        return bookActivityRepository.findById(id).map(bookActivityMapper::toResponse);
    }

    @Transactional
    public BookActivityResponse create(BookProgressRequest request) {
        var activity = bookActivityMapper.toEntity(request);
        bookActivityValidator.validate(activity);
        activity = bookActivityRepository.save(activity);

        KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class);
        var response = bookActivityMapper.toResponse(activity);
        bookActivityNotificationSubject.notify(response);
        return response;
    }

    @Transactional
    public void delete(Long id) {
        var activity = bookActivityRepository
                .findById(id)
                .orElseThrow(BookActivityNotFoundException::new);

        KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class);
        bookActivityRepository.delete(activity);
    }

}
