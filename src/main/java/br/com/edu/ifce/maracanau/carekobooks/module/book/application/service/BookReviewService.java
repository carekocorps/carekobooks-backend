package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookReviewMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookReviewQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookReviewValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewValidator bookReviewValidator;
    private final BookReviewMapper bookReviewMapper;

    @Transactional(readOnly = true)
    public ApplicationPage<BookReviewResponse> search(BookReviewQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookReviewRepository.findAll(specification, pageRequest).map(bookReviewMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public Optional<BookReviewResponse> find(Long id) {
        return bookReviewRepository.findById(id).map(bookReviewMapper::toResponse);
    }

    @Transactional
    public BookReviewResponse create(BookReviewRequest request) {
        var review = bookReviewMapper.toEntity(request);
        bookReviewValidator.validate(review);
        review = bookReviewRepository.save(review);

        KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class);
        return bookReviewMapper.toResponse(review);
    }

    @Transactional
    public BookReviewResponse update(Long id, BookReviewRequest request) {
        var review = bookReviewRepository
                .findById(id)
                .orElseThrow(BookReviewNotFoundException::new);

        KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class);
        bookReviewMapper.updateEntity(review, request);
        bookReviewValidator.validate(review);
        return bookReviewMapper.toResponse(bookReviewRepository.save(review));
    }

    @Transactional
    public void delete(Long id) {
        var review = bookReviewRepository
                .findById(id)
                .orElseThrow(BookReviewNotFoundException::new);

        KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class);
        bookReviewRepository.delete(review);
    }

}
