package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer.BaseBookThreadNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;

import java.util.ArrayList;
import java.util.List;

public class BookThreadNotificationSubject implements BaseNotificationSubject<BaseBookThreadNotificationObserver> {

    private final List<BaseBookThreadNotificationObserver> observers = new ArrayList<>();

    public void notify(BookThreadResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

    @Override
    public void addObserver(BaseBookThreadNotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BaseBookThreadNotificationObserver observer) {
        observers.remove(observer);
    }

}
