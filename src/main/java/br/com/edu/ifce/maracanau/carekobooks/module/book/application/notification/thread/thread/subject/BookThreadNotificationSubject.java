package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer.BaseBookThreadNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookThreadNotificationSubject implements BaseNotificationSubject<BaseBookThreadNotificationObserver> {

    private final List<BaseBookThreadNotificationObserver> observers;

    public void notify(BookThreadResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

}
