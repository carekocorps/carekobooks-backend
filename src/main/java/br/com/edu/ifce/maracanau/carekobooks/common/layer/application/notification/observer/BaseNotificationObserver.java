package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;

public interface BaseNotificationObserver<T extends BaseResponse> {

    void notify(T response);

}
