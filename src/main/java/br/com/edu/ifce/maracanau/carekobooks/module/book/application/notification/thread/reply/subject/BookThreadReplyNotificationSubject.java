package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer.BaseBookThreadReplyNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookThreadReplyNotificationSubject implements BaseNotificationSubject<BaseBookThreadReplyNotificationObserver> {

    private final List<BaseBookThreadReplyNotificationObserver> observers;

    public void notify(BookThreadReplyResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

}
