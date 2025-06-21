package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;

public interface BaseBookActivityNotificationObserver extends BaseNotificationObserver<BookActivityResponse> {

    void notify(BookActivityResponse response);

}
