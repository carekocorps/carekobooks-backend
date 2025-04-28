package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.notification.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.notification.observer.BaseNotificationObserver;

public interface BaseNotificationSubject<T extends BaseNotificationObserver> {

    void addObserver(T observer);
    void removeObserver(T observer);

}
