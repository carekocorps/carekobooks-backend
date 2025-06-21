package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookFactory {

    private static final Faker faker = new Faker();

    private BookFactory() {
    }

    public static Book updatedBook(Book book, BookRequest request) {
        var updatedBook = new Book();
        var genres = BookGenreFactory.validGenres(request.getGenres());
        updatedBook.setId(book.getId());
        updatedBook.setTitle(request.getTitle());
        updatedBook.setSynopsis(request.getSynopsis());
        updatedBook.setAuthorName(request.getAuthorName());
        updatedBook.setPublisherName(request.getPublisherName());
        updatedBook.setPublishedAt(request.getPublishedAt());
        updatedBook.setGenres(genres);
        updatedBook.setProgresses(List.of());
        updatedBook.setActivities(List.of());
        updatedBook.setReviews(List.of());
        updatedBook.setThreads(List.of());
        updatedBook.setImage(null);
        updatedBook.setCreatedAt(book.getCreatedAt());
        updatedBook.setUpdatedAt(LocalDateTime.now());
        return updatedBook;
    }

    public static Book validBookWithNullId(List<BookGenre> genres) {
        var book = new Book();
        book.setId(null);
        book.setTitle(faker.book().title());
        book.setSynopsis(faker.lorem().paragraph());
        book.setAuthorName(faker.book().author());
        book.setPublisherName(faker.book().publisher());
        book.setPublishedAt(LocalDate.now().minusDays(faker.number().numberBetween(0, 365 * 10)));
        book.setPageCount(faker.number().numberBetween(1, 1000));
        book.setImage(null);
        book.setGenres(genres);
        book.setProgresses(List.of());
        book.setActivities(List.of());
        book.setReviews(List.of());
        book.setThreads(List.of());
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(book.getCreatedAt());
        return book;
    }

    public static Book validBookWithNullId() {
        var genres = BookGenreFactory.validGenres(faker.number().numberBetween(1, 4));
        return validBookWithNullId(genres);
    }

    public static Book validBookWithNullIdAndEmptyGenres() {
        return validBookWithNullId(List.of());
    }

    public static Book validBookWithNullIdAndEmptyGenres(Image image) {
        var book = validBookWithNullId(List.of());
        book.setImage(image);
        return book;
    }

    public static Book validBook() {
        var book = validBookWithNullId();
        book.setId(faker.number().randomNumber());
        return book;
    }

    public static Book validBook(BookRequest request) {
        var book = new Book();
        var genres = BookGenreFactory.validGenres(request.getGenres());
        book.setTitle(request.getTitle());
        book.setSynopsis(request.getSynopsis());
        book.setAuthorName(request.getAuthorName());
        book.setPublisherName(request.getPublisherName());
        book.setPublishedAt(request.getPublishedAt());
        book.setPageCount(request.getPageCount());
        book.setGenres(genres);
        book.setProgresses(List.of());
        book.setActivities(List.of());
        book.setReviews(List.of());
        book.setThreads(List.of());
        book.setImage(null);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(book.getCreatedAt());
        return book;
    }

    public static Book validBook(Long id) {
        var book = validBook();
        book.setId(id);
        return book;
    }

    public static Book validBook(Long id, Integer pageCount) {
        var book = validBook();
        book.setId(id);
        book.setPageCount(pageCount);
        return book;
    }

    public static Book validBookWithImage(BookRequest request) {
        var book = validBook(request);
        book.setImage(ImageFactory.validImage());
        return book;
    }

    public static Book validBookWithImage() {
        var book = validBook();
        book.setImage(ImageFactory.validImage());
        return book;
    }

    public static Book validBookWithNullGenres() {
        var book = validBookWithNullIdAndEmptyGenres();
        book.setId(faker.number().randomNumber());
        book.setGenres(null);
        return book;
    }

    public static Book invalidBookByExceedingGenreLimit() {
        var genres = BookGenreFactory.validGenres(10);
        var book = validBookWithNullId(genres);
        book.setId(faker.number().randomNumber());
        return book;
    }

}
