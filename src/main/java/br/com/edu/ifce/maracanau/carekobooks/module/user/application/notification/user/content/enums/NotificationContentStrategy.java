package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.enums;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.factory.NotificationContentFactory;

import java.util.function.Function;

public enum NotificationContentStrategy {

    REGISTRATION(NotificationContentFactory::fromRegistrationOtp),
    PASSWORD(NotificationContentFactory::fromPasswordOtp),
    EMAIL(NotificationContentFactory::fromEmailOtp);

    private final Function<String, NotificationContent> strategy;

    NotificationContentStrategy(Function<String, NotificationContent> strategy) {
        this.strategy = strategy;
    }

    public NotificationContent build(String otp) {
        return strategy.apply(otp);
    }

}
