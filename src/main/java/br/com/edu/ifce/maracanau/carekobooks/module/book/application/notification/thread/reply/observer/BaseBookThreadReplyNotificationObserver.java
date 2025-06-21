package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;

public interface BaseBookThreadReplyNotificationObserver extends BaseNotificationObserver<BookThreadReplyResponse> {

    void notify(BookThreadReplyResponse response);

}
