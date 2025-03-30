package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookReviewMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookReviewDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookReviewSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookReviewValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.shared.layer.application.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookReviewService {

    private final BookRepository bookRepository;

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewValidator bookReviewValidator;
    private final BookReviewMapper bookReviewMapper;

    public ApplicationPage<BookReviewDTO> search(BookReviewSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookReviewRepository.findAll(specification, pageRequest).map(bookReviewMapper::toDTO));
    }

    public Optional<BookReviewDTO> findById(Long id) {
        return bookReviewRepository.findById(id).map(bookReviewMapper::toDTO);
    }

    @Transactional
    public BookReviewDTO create(BookReviewRequest request) {
        var bookReview = bookReviewMapper.toModel(request);
        bookReviewValidator.validate(bookReview);
        bookReview = bookReviewRepository.save(bookReview);

        if (bookRepository.existsById(request.getBookId())) {
            var reviewAverageScore = bookReviewRepository.findReviewAverageScoreByBookId(request.getBookId());
            bookReview.getBook().setReviewAverageScore(reviewAverageScore);
            bookRepository.updateReviewAverageScoreById(reviewAverageScore, request.getBookId());
        }

        return bookReviewMapper.toDTO(bookReview);
    }

    @Transactional
    public void update(Long id, BookReviewRequest request) {
        var bookReview = bookReviewRepository.findById(id).orElse(null);
        if (bookReview == null) {
            throw new NotFoundException("Book Review not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(bookReview.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to create this book review");
        }

        bookReviewMapper.updateEntity(bookReview, request);
        bookReviewValidator.validate(bookReview);
        bookReviewRepository.save(bookReview);

        if (bookRepository.existsById(request.getBookId())) {
            var reviewAverageScore = bookReviewRepository.findReviewAverageScoreByBookId(request.getBookId());
            bookRepository.updateReviewAverageScoreById(reviewAverageScore, request.getBookId());
        }
    }

    @Transactional
    public void deleteById(Long id) {
        var bookReview = bookReviewRepository.findById(id).orElse(null);
        if (bookReview == null) {
            throw new NotFoundException("Book Review not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(bookReview.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this book review");
        }

        bookReviewRepository.deleteById(id);
    }

}
