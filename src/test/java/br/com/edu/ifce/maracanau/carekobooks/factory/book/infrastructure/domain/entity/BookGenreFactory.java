package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookGenreFactory {

    private static final Faker faker = new Faker();

    private BookGenreFactory() {
    }

    public static BookGenre updatedGenre(BookGenre genre, BookGenreRequest request) {
        var updatedGenre = new BookGenre();
        updatedGenre.setId(genre.getId());
        updatedGenre.setName(request.getName());
        updatedGenre.setDisplayName(request.getDisplayName());
        updatedGenre.setDescription(request.getDescription());
        updatedGenre.setBooks(genre.getBooks());
        updatedGenre.setCreatedAt(genre.getCreatedAt());
        updatedGenre.setUpdatedAt(LocalDateTime.now());
        return updatedGenre;
    }

    public static BookGenre validGenreWithNullId() {
        var genre = new BookGenre();
        var genreName = faker.book().genre();
        genre.setId(null);
        genre.setName(genreName.toLowerCase().replaceAll("[^a-z0-9]", ""));
        genre.setDisplayName(genreName);
        genre.setDescription(faker.lorem().paragraph());
        genre.setBooks(List.of());
        genre.setCreatedAt(LocalDateTime.now());
        genre.setUpdatedAt(genre.getCreatedAt());
        return genre;
    }

    public static BookGenre validGenre() {
        var genre = validGenreWithNullId();
        genre.setId(faker.number().randomNumber());
        return genre;
    }

    public static BookGenre validGenre(BookGenreRequest request) {
        var genre = new BookGenre();
        genre.setId(faker.number().randomNumber());
        genre.setName(request.getName());
        genre.setDisplayName(request.getDisplayName());
        genre.setDescription(request.getDescription());
        genre.setBooks(List.of());
        genre.setCreatedAt(LocalDateTime.now());
        genre.setUpdatedAt(genre.getCreatedAt());
        return genre;
    }

    public static BookGenre validGenre(Long id, String name) {
        var genre = validGenre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }

    public static BookGenre validGenre(String name) {
        var genre = validGenre();
        genre.setName(name);
        return genre;
    }

    public static List<BookGenre> validGenres(List<String> genreNames) {
        return genreNames
                .stream()
                .map(BookGenreFactory::validGenre)
                .collect(Collectors.toMap(BookGenre::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

    public static List<BookGenre> validGenres(int numGenres) {
        return IntStream
                .range(0, numGenres)
                .mapToObj(i -> BookGenreFactory.validGenre())
                .collect(Collectors.toMap(BookGenre::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

}
