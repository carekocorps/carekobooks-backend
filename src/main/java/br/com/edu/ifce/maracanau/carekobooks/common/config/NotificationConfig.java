package br.com.edu.ifce.maracanau.carekobooks.common.config;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.observer.BookActivityWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.subject.BookActivityNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.observer.UserEmailNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.subject.UserNotificationSubject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class NotificationConfig {

    @Bean
    public BookActivityNotificationSubject bookActivitySubject(BookActivityWebSocketNotificationObserver bookActivityWebSocketObserver) {
        return new BookActivityNotificationSubject(List.of(bookActivityWebSocketObserver));
    }

    @Bean
    public UserNotificationSubject userSubject(UserEmailNotificationObserver userEmailObserver) {
        return new UserNotificationSubject(List.of(userEmailObserver));
    }

}
