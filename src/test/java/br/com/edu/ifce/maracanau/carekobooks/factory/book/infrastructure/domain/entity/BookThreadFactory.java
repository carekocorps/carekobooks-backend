package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class BookThreadFactory {

    private static final Faker faker = new Faker();

    private BookThreadFactory() {
    }

    public static BookThread updatedThread(BookThread thread, BookThreadRequest request) {
        var updatedThread = new BookThread();
        updatedThread.setId(thread.getId());
        updatedThread.setTitle(request.getTitle());
        updatedThread.setDescription(request.getDescription());
        updatedThread.setBook(BookFactory.validBook(request.getBookId()));
        updatedThread.setUser(UserFactory.validUser(request.getUsername()));
        updatedThread.setReplies(thread.getReplies());
        thread.setCreatedAt(thread.getCreatedAt());
        thread.setUpdatedAt(LocalDateTime.now());
        return updatedThread;
    }

    public static BookThread validThread(BookThreadRequest request) {
        var thread = new BookThread();
        thread.setId(Math.abs(new Random().nextLong()) + 1);
        thread.setTitle(request.getTitle());
        thread.setDescription(request.getDescription());
        thread.setBook(BookFactory.validBook(request.getBookId()));
        thread.setUser(UserFactory.validUser(request.getUsername()));
        thread.setReplies(List.of());
        thread.setCreatedAt(LocalDateTime.now());
        thread.setUpdatedAt(thread.getCreatedAt());
        return thread;
    }

    public static BookThread validThread() {
        var thread = new BookThread();
        thread.setId(faker.number().randomNumber());
        thread.setTitle(faker.lorem().sentence());
        thread.setDescription(faker.lorem().paragraph());
        thread.setBook(BookFactory.validBook());
        thread.setUser(UserFactory.validUser());
        thread.setReplies(List.of());
        thread.setCreatedAt(LocalDateTime.now());
        thread.setUpdatedAt(thread.getCreatedAt());
        return thread;
    }

    public static BookThread validThread(Long id) {
        var thread = validThread();
        thread.setId(id);
        return thread;
    }

    public static BookThread invalidReviewByEmptyUser() {
        var thread = validThread();
        thread.setUser(null);
        return thread;
    }

    public static BookThread invalidReviewByEmptyBook() {
        var thread = validThread();
        thread.setBook(null);
        return thread;
    }

}
