package br.com.edu.ifce.maracanau.carekobooks.module.book.api.config;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.observer.BookActivityWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.subject.BookActivityNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.observer.BookReviewWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.subject.BookReviewNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer.BookThreadReplyWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.subject.BookThreadReplyNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer.BookThreadWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject.BookThreadNotificationSubject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class NotificationSubjectConfig {

    @Bean
    public BookActivityNotificationSubject bookActivitySubject(BookActivityWebSocketNotificationObserver observer) {
        return new BookActivityNotificationSubject(List.of(observer));
    }

    @Bean
    public BookReviewNotificationSubject bookReviewSubject(BookReviewWebSocketNotificationObserver observer) {
        return new BookReviewNotificationSubject(List.of(observer));
    }

    @Bean
    public BookThreadNotificationSubject bookThreadSubject(BookThreadWebSocketNotificationObserver observer) {
        return new BookThreadNotificationSubject(List.of(observer));
    }

    @Bean
    public BookThreadReplyNotificationSubject bookThreadReplyNotificationSubject(BookThreadReplyWebSocketNotificationObserver observer) {
        return new BookThreadReplyNotificationSubject(List.of(observer));
    }

}
