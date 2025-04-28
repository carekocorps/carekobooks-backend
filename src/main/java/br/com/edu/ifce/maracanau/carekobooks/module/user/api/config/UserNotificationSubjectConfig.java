package br.com.edu.ifce.maracanau.carekobooks.module.user.api.config;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.observer.UserEmailNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.subject.UserNotificationSubject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserNotificationSubjectConfig {

    @Bean
    public UserNotificationSubject userSubject(UserEmailNotificationObserver observer) {
        var subject = new UserNotificationSubject();
        subject.addObserver(observer);
        return subject;
    }

}
