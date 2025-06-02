package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.observer.BaseUserNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.AuthUserResponse;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationSubject implements BaseNotificationSubject<BaseUserNotificationObserver> {

    private final List<BaseUserNotificationObserver> observers = new ArrayList<>();

    public void notify(AuthUserResponse response, NotificationContent content) {
        observers.forEach(observer -> observer.notify(response, content));
    }

    @Override
    public void addObserver(BaseUserNotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BaseUserNotificationObserver observer) {
        observers.remove(observer);
    }

}
