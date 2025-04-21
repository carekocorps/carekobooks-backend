package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.observer.BaseBookActivityNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookActivityNotificationSubject implements BaseNotificationSubject<BaseBookActivityNotificationObserver> {

    private final List<BaseBookActivityNotificationObserver> observers;

    public void notify(BookActivityResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

}
