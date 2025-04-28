package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.notification.thread.thread.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;

public interface BaseBookThreadNotificationObserver extends BaseNotificationObserver {

    void notify(BookThreadResponse response);

}
