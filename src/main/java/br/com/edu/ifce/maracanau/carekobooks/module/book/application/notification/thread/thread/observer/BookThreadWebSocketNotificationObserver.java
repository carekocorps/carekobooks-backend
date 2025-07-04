package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookThreadWebSocketNotificationObserver implements BaseBookThreadNotificationObserver {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notify(BookThreadResponse response) {
        var bookId = response.getBook().getId();
        var destination = String.format("/topic/books/%d/threads", bookId);
        messagingTemplate.convertAndSend(destination, response);
    }

}
