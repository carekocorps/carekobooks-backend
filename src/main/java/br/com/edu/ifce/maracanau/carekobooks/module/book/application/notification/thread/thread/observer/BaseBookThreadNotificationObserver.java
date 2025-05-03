package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;

public interface BaseBookThreadNotificationObserver extends BaseNotificationObserver {

    void notify(BookThreadResponse response);

}
