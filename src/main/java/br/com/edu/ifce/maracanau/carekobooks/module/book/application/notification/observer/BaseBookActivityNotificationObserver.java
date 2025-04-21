package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;

public interface BaseBookActivityNotificationObserver extends BaseNotificationObserver {

    void notify(BookActivity bookActivity);

}
