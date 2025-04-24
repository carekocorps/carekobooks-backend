package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.observer.BaseUserNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationSubject implements BaseNotificationSubject<BaseUserNotificationObserver> {

    private final List<BaseUserNotificationObserver> observers = new ArrayList<>();

    public void notify(UserResponse response, NotificationContent content) {
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
