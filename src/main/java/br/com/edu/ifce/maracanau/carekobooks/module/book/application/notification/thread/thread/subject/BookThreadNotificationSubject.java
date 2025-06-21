package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer.BaseBookThreadNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BookThreadNotificationSubject implements BaseNotificationSubject<BookThreadResponse, BaseBookThreadNotificationObserver> {

    private final List<BaseBookThreadNotificationObserver> observers;

    public void notify(BookThreadResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

}
