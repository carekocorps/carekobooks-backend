package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer.BaseBookThreadReplyNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadReplyResponse;

import java.util.ArrayList;
import java.util.List;

public class BookThreadReplyNotificationSubject implements BaseNotificationSubject<BaseBookThreadReplyNotificationObserver> {

    private final List<BaseBookThreadReplyNotificationObserver> observers = new ArrayList<>();

    public void notify(BookThreadReplyResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

    @Override
    public void addObserver(BaseBookThreadReplyNotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BaseBookThreadReplyNotificationObserver observer) {
        observers.remove(observer);
    }

}
