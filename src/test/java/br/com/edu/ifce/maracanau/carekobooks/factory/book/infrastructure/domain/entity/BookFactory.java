package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookFactory {

    private static final Faker faker = new Faker();

    private BookFactory() {
    }

    public static Book updatedBook(Book book, BookRequest request) {
        var updatedBook = new Book();
        var genres = request
                .getGenres()
                .stream()
                .map(BookGenreFactory::validGenre)
                .collect(Collectors.toMap(BookGenre::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();

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

    public static Book validBook(BookRequest request) {
        var book = new Book();
        var genres = request
                .getGenres()
                .stream()
                .map(BookGenreFactory::validGenre)
                .collect(Collectors.toMap(BookGenre::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();

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

    public static Book validBookWithImage(BookRequest request) {
        var book = validBook(request);
        book.setImage(ImageFactory.validImage());
        return book;
    }

    public static Book validBook(int numGenres) {
        var book = new Book();
        var genres = IntStream
                .range(0, numGenres)
                .mapToObj(i -> BookGenreFactory.validGenre())
                .collect(Collectors.toMap(BookGenre::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();

        book.setId(faker.number().randomNumber());
        book.setTitle(faker.book().title());
        book.setSynopsis(faker.lorem().paragraph());
        book.setAuthorName(faker.book().author());
        book.setPublisherName(faker.book().publisher());
        book.setPageCount(faker.number().numberBetween(0, 1000));
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

    public static Book validBook() {
        return validBook(faker.number().numberBetween(1, 4));
    }

    public static Book validBookWithImage() {
        var book = validBook();
        book.setImage(ImageFactory.validImage());
        return book;
    }

    public static Book validBook(Long id) {
        var book = validBook();
        book.setId(id);
        return book;
    }

    public static Book validBook(Integer pageCount) {
        var book = validBook();
        book.setPageCount(pageCount);
        return book;
    }

    public static Book validBook(Long id, Integer pageCount) {
        var book = validBook();
        book.setId(id);
        book.setPageCount(pageCount);
        return book;
    }

}
