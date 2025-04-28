package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.notification.activity.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;

public interface BaseBookActivityNotificationObserver extends BaseNotificationObserver {

    void notify(BookActivityResponse response);

}
