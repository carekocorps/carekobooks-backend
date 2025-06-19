package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.observer.BaseBookReviewNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;

import java.util.ArrayList;
import java.util.List;

public class BookReviewNotificationSubject implements BaseNotificationSubject<BaseBookReviewNotificationObserver> {

    private final List<BaseBookReviewNotificationObserver> observers = new ArrayList<>();

    public void notify(BookReviewResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

    @Override
    public void addObserver(BaseBookReviewNotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BaseBookReviewNotificationObserver observer) {
        observers.remove(observer);
    }

}
