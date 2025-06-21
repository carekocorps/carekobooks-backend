package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer.BaseBookThreadReplyNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BookThreadReplyNotificationSubject implements BaseNotificationSubject<BookThreadReplyResponse, BaseBookThreadReplyNotificationObserver> {

    private final List<BaseBookThreadReplyNotificationObserver> observers;

    public void notify(BookThreadReplyResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

}
