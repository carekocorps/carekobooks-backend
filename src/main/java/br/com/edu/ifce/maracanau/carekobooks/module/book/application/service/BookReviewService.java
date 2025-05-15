package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookReviewMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookReviewQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookReviewValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.annotation.AuthenticatedUserMatchRequired;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookReviewService {

    private final BookService bookService;

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewValidator bookReviewValidator;
    private final BookReviewMapper bookReviewMapper;

    public ApplicationPage<BookReviewResponse> search(BookReviewQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookReviewRepository.findAll(specification, pageRequest).map(bookReviewMapper::toResponse));
    }

    public Optional<BookReviewResponse> find(Long id) {
        return bookReviewRepository.findById(id).map(bookReviewMapper::toResponse);
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "request", exception = BookReviewModificationForbiddenException.class)
    public BookReviewResponse create(BookReviewRequest request) {
        var review = bookReviewMapper.toModel(request);
        bookReviewValidator.validate(review);
        var response = bookReviewMapper.toResponse(bookReviewRepository.save(review));

        var reviewAverageScore = bookReviewRepository.calculateReviewAverageScore(request.getBookId());
        bookService.changeReviewAverageScore(request.getBookId(), reviewAverageScore);
        response.getBook().setReviewAverageScore(reviewAverageScore);
        return response;
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "request", exception = BookReviewModificationForbiddenException.class)
    public BookReviewResponse update(Long id, BookReviewRequest request) {
        var review = bookReviewRepository
                .findById(id)
                .orElseThrow(BookReviewNotFoundException::new);

        bookReviewMapper.updateModel(review, request);
        bookReviewValidator.validate(review);
        var response = bookReviewMapper.toResponse(bookReviewRepository.save(review));

        var reviewAverageScore = bookReviewRepository.calculateReviewAverageScore(request.getBookId());
        bookService.changeReviewAverageScore(request.getBookId(), reviewAverageScore);
        response.getBook().setReviewAverageScore(reviewAverageScore);
        return response;
    }

    @Transactional
    public void delete(Long id) {
        var review = bookReviewRepository
                .findById(id)
                .orElseThrow(BookReviewNotFoundException::new);

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(review.getUser().getUsername())) {
            throw new BookReviewModificationForbiddenException();
        }

        bookReviewRepository.deleteById(id);
    }

}
