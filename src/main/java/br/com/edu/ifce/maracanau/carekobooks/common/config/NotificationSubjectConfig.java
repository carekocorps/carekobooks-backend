package br.com.edu.ifce.maracanau.carekobooks.common.config;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notication.observer.BookActivityWebSocketObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notication.subject.BookActivitySubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationSubjectConfig {

    @Autowired
    private BookActivityWebSocketObserver bookActivityWebSocketObserver;

    @Bean
    public BookActivitySubject bookActivitySubject() {
        var bookActivitySubject = new BookActivitySubject();
        bookActivitySubject.addObserver(bookActivityWebSocketObserver);
        return bookActivitySubject;
    }

}
