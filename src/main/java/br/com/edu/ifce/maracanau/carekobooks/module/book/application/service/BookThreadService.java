package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject.BookThreadNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookThreadQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookThreadValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookThreadService {

    private final UserContextProvider userContextProvider;

    private final BookThreadRepository bookThreadRepository;
    private final BookThreadValidator bookThreadValidator;
    private final BookThreadMapper bookThreadMapper;
    private final BookThreadNotificationSubject bookThreadNotificationSubject;

    @Transactional(readOnly = true)
    public ApplicationPage<BookThreadResponse> search(BookThreadQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookThreadRepository.findAll(specification, pageRequest).map(bookThreadMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public Optional<BookThreadResponse> find(Long id) {
        return bookThreadRepository.findById(id).map(bookThreadMapper::toResponse);
    }

    @Transactional
    public BookThreadResponse create(BookThreadRequest request) {
        userContextProvider.assertAuthorized(request.getUsername(), BookThreadModificationForbiddenException.class);
        var thread = bookThreadMapper.toEntity(request);
        bookThreadValidator.validate(thread);

        var response = bookThreadMapper.toResponse(bookThreadRepository.save(thread));
        bookThreadNotificationSubject.notify(response);
        return response;
    }

    @Transactional
    public BookThreadResponse update(Long id, BookThreadRequest request) {
        userContextProvider.assertAuthorized(request.getUsername(), BookThreadModificationForbiddenException.class);
        var thread = bookThreadRepository
                .findById(id)
                .orElseThrow(BookThreadNotFoundException::new);

        bookThreadMapper.updateEntity(thread, request);
        bookThreadValidator.validate(thread);
        return bookThreadMapper.toResponse(bookThreadRepository.save(thread));
    }

    @Transactional
    public void delete(Long id) {
        var thread = bookThreadRepository
                .findById(id)
                .orElseThrow(BookThreadNotFoundException::new);

        userContextProvider.assertAuthorized(thread.getUser().getUsername(), BookThreadModificationForbiddenException.class);
        bookThreadRepository.delete(thread);
    }

}
