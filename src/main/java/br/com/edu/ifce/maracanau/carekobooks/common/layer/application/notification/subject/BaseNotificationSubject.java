package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;

import java.util.List;

public interface BaseNotificationSubject<T extends BaseResponse, R extends BaseNotificationObserver<T>> {

    void notify(T response);
    List<R> getObservers();

}
