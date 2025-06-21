package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.notification.thread.reply.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookThreadReplyResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.observer.BookThreadReplyWebSocketNotificationObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookThreadReplyWebSocketNotificationObserverTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private BookThreadReplyWebSocketNotificationObserver bookThreadReplyWebSocketNotificationObserver;

    @Test
    void notify_withValidReplyResponse_shouldSucceed() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();
        var response = BookThreadReplyResponseFactory.validResponse(reply);

        doNothing()
                .when(messagingTemplate)
                .convertAndSend(any(String.class), eq(response));

        // Act && Assert
        assertThatCode(() -> bookThreadReplyWebSocketNotificationObserver.notify(response)).doesNotThrowAnyException();
        verify(messagingTemplate, times(1)).convertAndSend(any(String.class), eq(response));
    }

}
