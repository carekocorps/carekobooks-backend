package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;

public interface BaseUserNotificationObserver extends BaseNotificationObserver {

    void notify(UserResponse response, NotificationContent content);

}
