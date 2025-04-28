package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.content.enums;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.notification.content.factory.NotificationContentFactory;

import java.util.function.Function;

public enum NotificationContentStrategy {

    REGISTRATION(NotificationContentFactory::buildFromRegistrationOtp),
    PASSWORD(NotificationContentFactory::buildFromPasswordOtp),
    EMAIL(NotificationContentFactory::buildFromEmailOtp);

    private final Function<String, NotificationContent> strategy;

    NotificationContentStrategy(Function<String, NotificationContent> strategy) {
        this.strategy = strategy;
    }

    public NotificationContent build(String otp) {
        return strategy.apply(otp);
    }

}
