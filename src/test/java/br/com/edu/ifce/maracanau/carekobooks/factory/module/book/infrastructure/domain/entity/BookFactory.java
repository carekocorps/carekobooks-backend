package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class BookFactory {

    private BookFactory() {
    }

    public static Book validBook() {
        var random = new Random();
        var book = new Book();
        book.setId(random.nextLong());
        book.setTitle(UUID.randomUUID().toString());
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

    public static Book validBook(Integer pageCount) {
        var book = validBook();
        book.setPageCount(pageCount);
        return book;
    }

}
