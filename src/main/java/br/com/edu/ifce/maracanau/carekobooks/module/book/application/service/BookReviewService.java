package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookReviewMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookReviewSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookReviewValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.ApplicationPage;
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

    public ApplicationPage<BookReviewResponse> search(BookReviewSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookReviewRepository.findAll(specification, pageRequest).map(bookReviewMapper::toResponse));
    }

    public Optional<BookReviewResponse> findById(Long id) {
        return bookReviewRepository.findById(id).map(bookReviewMapper::toResponse);
    }

    @Transactional
    public BookReviewResponse create(BookReviewRequest request) {
        var bookReview = bookReviewMapper.toModel(request);
        bookReviewValidator.validate(bookReview);
        bookReview = bookReviewRepository.save(bookReview);

        var reviewAverageScore = bookReviewRepository.findReviewAverageScoreByBookId(request.getBookId());
        bookService.updateReviewAverageScoreById(request.getBookId(), reviewAverageScore);
        bookReview.getBook().setReviewAverageScore(reviewAverageScore);

        return bookReviewMapper.toResponse(bookReview);
    }

    @Transactional
    public BookReviewResponse update(Long id, BookReviewRequest request) {
        var bookReview = bookReviewRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Review not found"));

        bookReviewMapper.updateModel(bookReview, request);
        bookReviewValidator.validate(bookReview);
        bookReviewRepository.save(bookReview);

        if (!UserContextProvider.isCurrentUserAuthorized(bookReview.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to create this book review");
        }

        var reviewAverageScore = bookReviewRepository.findReviewAverageScoreByBookId(request.getBookId());
        bookService.updateReviewAverageScoreById(request.getBookId(), reviewAverageScore);
        bookReview.getBook().setReviewAverageScore(reviewAverageScore);
        return bookReviewMapper.toResponse(bookReview);
    }

    @Transactional
    public void deleteById(Long id) {
        var bookReview = bookReviewRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Book Review not found"));

        if (!UserContextProvider.isCurrentUserAuthorized(bookReview.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this book review");
        }

        bookReviewRepository.deleteById(id);
    }

}
