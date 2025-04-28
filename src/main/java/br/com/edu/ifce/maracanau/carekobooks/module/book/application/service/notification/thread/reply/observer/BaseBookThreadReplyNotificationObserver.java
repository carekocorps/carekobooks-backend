package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.notification.thread.reply.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;

public interface BaseBookThreadReplyNotificationObserver extends BaseNotificationObserver {

    void notify(BookThreadReplyResponse response);

}
