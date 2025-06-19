package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookReviewWebSocketNotificationObserver implements BaseBookReviewNotificationObserver {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notify(BookReviewResponse response) {
        var destination = String.format("/topic/books/%d/reviews", response.getBook().getId());
        messagingTemplate.convertAndSend(destination, response);
    }

}
