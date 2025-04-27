package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject.BookThreadNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookThreadQuery;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookThreadValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookThreadService {

    private final BookThreadRepository bookThreadRepository;
    private final BookThreadValidator bookThreadValidator;
    private final BookThreadMapper bookThreadMapper;
    private final BookThreadNotificationSubject bookThreadNotificationSubject;

    public ApplicationPage<BookThreadResponse> search(BookThreadQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookThreadRepository.findAll(specification, pageRequest).map(bookThreadMapper::toResponse));
    }

    public Optional<BookThreadResponse> find(Long id) {
        return bookThreadRepository.findById(id).map(bookThreadMapper::toResponse);
    }

    @Transactional
    public BookThreadResponse create(BookThreadRequest request) {
        var thread = bookThreadMapper.toModel(request);
        bookThreadValidator.validate(thread);

        var response = bookThreadMapper.toResponse(bookThreadRepository.save(thread));
        bookThreadNotificationSubject.notify(response);
        return response;
    }

    @Transactional
    public BookThreadResponse update(Long id, BookThreadRequest request) {
        var thread = bookThreadRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Thread not found"));

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(thread.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this thread");
        }

        bookThreadMapper.updateModel(thread, request);
        bookThreadValidator.validate(thread);
        bookThreadRepository.save(thread);
        return bookThreadMapper.toResponse(thread);
    }

    @Transactional
    public void delete(Long id) {
        var thread = bookThreadRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Thread not found"));

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(thread.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this thread");
        }

        bookThreadRepository.deleteById(id);
    }

}
