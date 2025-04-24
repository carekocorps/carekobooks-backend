package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.subject.BookActivityNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookActivityQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookActivityService {

    private final BookActivityRepository bookActivityRepository;
    private final BookActivityMapper bookActivityMapper;
    private final BookActivityValidator bookActivityValidator;
    private final BookActivityNotificationSubject bookActivityNotificationSubject;

    public ApplicationPage<BookActivityResponse> search(BookActivityQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookActivityRepository.findAll(specification, pageRequest).map(bookActivityMapper::toResponse));
    }

    public Optional<BookActivityResponse> find(Long id) {
        return bookActivityRepository.findById(id).map(bookActivityMapper::toResponse);
    }

    @Transactional
    public BookActivityResponse create(BookProgressRequest request) {
        var activity = bookActivityMapper.toModel(request);
        bookActivityValidator.validate(activity);
        bookActivityRepository.save(activity);

        var response = bookActivityMapper.toResponse(activity);
        bookActivityNotificationSubject.notify(response);
        return response;
    }

    @Transactional
    public void delete(Long id) {
        var activity = bookActivityRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        if (UserContextProvider.isUserUnauthorized(activity.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this book activity");
        }

        bookActivityRepository.deleteById(id);
    }

}
