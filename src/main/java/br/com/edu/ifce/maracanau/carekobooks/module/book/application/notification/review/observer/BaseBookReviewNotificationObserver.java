package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;

public interface BaseBookReviewNotificationObserver extends BaseNotificationObserver<BookReviewResponse> {

    void notify(BookReviewResponse response);

}
