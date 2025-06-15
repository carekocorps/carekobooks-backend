package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;

public class BookReviewFactory {

    private static final Faker faker = new Faker();

    private BookReviewFactory() {
    }

    public static BookReview updatedReview(BookReview review, BookReviewRequest request) {
        var updatedReview = new BookReview();
        updatedReview.setId(review.getId());
        updatedReview.setTitle(request.getTitle());
        updatedReview.setContent(request.getContent());
        updatedReview.setScore(request.getScore());
        updatedReview.setUser(UserFactory.validUser(request.getUsername()));
        updatedReview.setBook(BookFactory.validBook(request.getBookId()));
        updatedReview.setCreatedAt(review.getCreatedAt());
        updatedReview.setUpdatedAt(LocalDateTime.now());
        return updatedReview;
    }

    public static BookReview validReviewWithNullId(Book book, User user) {
        var review = new BookReview();
        review.setId(null);
        review.setTitle(faker.lorem().sentence());
        review.setContent(faker.lorem().paragraph());
        review.setScore(faker.number().numberBetween(0, 100));
        review.setUser(user);
        review.setBook(book);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(review.getCreatedAt());
        return review;
    }

    public static BookReview validReviewWithNullId() {
        var book = BookFactory.validBook();
        var user = UserFactory.validUser();
        return validReviewWithNullId(book, user);
    }

    public static BookReview validReview() {
        var review = validReviewWithNullId();
        review.setId(faker.number().randomNumber());
        return review;
    }

    public static BookReview validReview(BookReviewRequest request) {
        var review = new BookReview();
        review.setId(faker.number().randomNumber());
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setScore(request.getScore());
        review.setUser(UserFactory.validUser(request.getUsername()));
        review.setBook(BookFactory.validBook(request.getBookId()));
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(review.getCreatedAt());
        return review;
    }

    public static BookReview invalidReviewByEmptyUser() {
        var review = validReview();
        review.setUser(null);
        return review;
    }

    public static BookReview invalidReviewByEmptyBook() {
        var review = validReview();
        review.setBook(null);
        return review;
    }

}
