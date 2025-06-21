package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.observer.BaseBookReviewNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BookReviewNotificationSubject implements BaseNotificationSubject<BookReviewResponse, BaseBookReviewNotificationObserver> {

    private final List<BaseBookReviewNotificationObserver> observers;

    public void notify(BookReviewResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

}
