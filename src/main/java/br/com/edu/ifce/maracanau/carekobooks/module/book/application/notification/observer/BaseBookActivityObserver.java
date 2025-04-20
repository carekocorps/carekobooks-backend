package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;

public interface BaseBookActivityObserver extends BaseObserver {

    void notify(BookActivityResponse response);

}
