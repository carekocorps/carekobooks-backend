package br.com.edu.ifce.maracanau.carekobooks.common.config;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.observer.BookActivityWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.subject.BookActivityNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer.BookThreadReplyWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.subject.BookThreadReplyNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer.BookThreadWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject.BookThreadNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.observer.UserEmailNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.subject.UserNotificationSubject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationSubjectConfig {

    @Bean
    public BookActivityNotificationSubject bookActivitySubject(BookActivityWebSocketNotificationObserver observer) {
        var subject = new BookActivityNotificationSubject();
        subject.addObserver(observer);
        return subject;
    }

    @Bean
    public BookThreadNotificationSubject bookThreadSubject(BookThreadWebSocketNotificationObserver observer) {
        var subject = new BookThreadNotificationSubject();
        subject.addObserver(observer);
        return subject;
    }

    @Bean
    public BookThreadReplyNotificationSubject bookThreadReplyNotificationSubject(BookThreadReplyWebSocketNotificationObserver observer) {
        var subject = new BookThreadReplyNotificationSubject();
        subject.addObserver(observer);
        return subject;
    }

    @Bean
    public UserNotificationSubject userSubject(UserEmailNotificationObserver observer) {
        var subject = new UserNotificationSubject();
        subject.addObserver(observer);
        return subject;
    }

}
