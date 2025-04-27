package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadReplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookThreadReplyWebSocketNotificationObserver implements BaseBookThreadReplyNotificationObserver {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notify(BookThreadReplyResponse response) {
        var bookId = response.getThread().getBook().getId();
        var threadId = response.getThread().getId();
        var destination = "/topic/books/" + bookId + "/threads/" + threadId;
        messagingTemplate.convertAndSend(destination, response);
    }

}
