package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;

public interface BaseUserNotificationObserver extends BaseNotificationObserver {

    void notify(UserResponse response, NotificationContent content);

}
