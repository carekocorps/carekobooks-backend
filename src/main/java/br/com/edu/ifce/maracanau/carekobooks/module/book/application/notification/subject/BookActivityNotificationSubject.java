package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.observer.BaseBookActivityNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;

import java.util.ArrayList;
import java.util.List;

public class BookActivityNotificationSubject implements BaseNotificationSubject<BaseBookActivityNotificationObserver> {

    private final List<BaseBookActivityNotificationObserver> observers = new ArrayList<>();

    public void notify(BookActivityResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

    @Override
    public void addObserver(BaseBookActivityNotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BaseBookActivityNotificationObserver observer) {
        observers.remove(observer);
    }

}
