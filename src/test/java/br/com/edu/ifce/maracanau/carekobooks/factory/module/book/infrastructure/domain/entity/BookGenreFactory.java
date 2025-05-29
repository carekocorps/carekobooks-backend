package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BookGenreFactory {

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

    public static BookGenre validGenre(BookGenreRequest request) {
        var genre = new BookGenre();
        genre.setId(new Random().nextLong());
        genre.setName(request.getName());
        genre.setDisplayName(request.getDisplayName());
        genre.setDescription(request.getDescription());
        genre.setBooks(List.of());
        genre.setCreatedAt(LocalDateTime.now());
        genre.setUpdatedAt(genre.getCreatedAt());
        return genre;
    }

    public static BookGenre validGenre() {
        var genre = new BookGenre();
        genre.setId(new Random().nextLong());
        genre.setName(UUID.randomUUID().toString().replace("-", ""));
        genre.setDisplayName(UUID.randomUUID().toString());
        genre.setDescription(UUID.randomUUID().toString());
        genre.setBooks(List.of());
        genre.setCreatedAt(LocalDateTime.now());
        genre.setUpdatedAt(genre.getCreatedAt());
        return genre;
    }

    public static BookGenre validGenre(String name) {
        var genre = validGenre();
        genre.setName(name);
        return genre;
    }

    public static BookGenre validGenre(Long id, String name) {
        var genre = validGenre(name);
        genre.setId(id);
        return genre;
    }

}
