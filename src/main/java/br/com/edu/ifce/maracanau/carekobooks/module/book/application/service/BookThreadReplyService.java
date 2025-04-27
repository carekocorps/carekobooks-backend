package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.subject.BookThreadReplyNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadReplyMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookThreadReplyQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookThreadReplyValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadReplyRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookThreadReplyService {

    private final BookThreadReplyRepository bookThreadReplyRepository;
    private final BookThreadReplyValidator bookThreadReplyValidator;
    private final BookThreadReplyMapper bookThreadReplyMapper;
    private final BookThreadReplyNotificationSubject bookThreadReplyNotificationSubject;

    @Transactional
    public ApplicationPage<BookThreadReplyResponse> search(BookThreadReplyQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookThreadReplyRepository.findAll(specification, pageRequest).map(bookThreadReplyMapper::toResponse));
    }

    @Transactional
    public Optional<BookThreadReplyResponse> find(Long id) {
        return bookThreadReplyRepository.findById(id).map(bookThreadReplyMapper::toResponse);
    }

    @Transactional
    public BookThreadReplyResponse create(BookThreadReplyRequest request) {
        var reply = bookThreadReplyMapper.toModel(request);
        bookThreadReplyValidator.validate(reply);

        var response = bookThreadReplyMapper.toResponse(bookThreadReplyRepository.save(reply));
        bookThreadReplyNotificationSubject.notify(response);
        return response;
    }

    @Transactional
    public BookThreadReplyResponse update(Long id, BookThreadReplyRequest request) {
        var reply = bookThreadReplyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Thread Reply not found"));

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(reply.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this thread reply");
        }

        bookThreadReplyMapper.updateModel(reply, request);
        bookThreadReplyValidator.validate(reply);
        return bookThreadReplyMapper.toResponse(bookThreadReplyRepository.save(reply));
    }

    @Transactional
    public BookThreadReplyResponse createChild(Long id, BookThreadReplyRequest request) {
        var parent = bookThreadReplyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Thread Reply not found"));

        var child = bookThreadReplyMapper.toModel(request);
        child.setParent(parent);
        bookThreadReplyValidator.validate(child);

        var response = bookThreadReplyMapper.toResponse(bookThreadReplyRepository.save(child));
        bookThreadReplyNotificationSubject.notify(response);
        return response;
    }

    @Transactional
    public void delete(Long id) {
        var reply = bookThreadReplyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Thread Reply not found"));

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(reply.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this thread reply");
        }

        bookThreadReplyRepository.deleteById(id);
    }

}
