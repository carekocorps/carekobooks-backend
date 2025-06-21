package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.notification.thread.thread.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookThreadResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.observer.BookThreadWebSocketNotificationObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(SpringExtension.class)
class BookThreadWebSocketNotificationObserverTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private BookThreadWebSocketNotificationObserver bookThreadWebSocketNotificationObserver;

    @Test
    void notify_withValidThreadResponse_shouldSucceed() {
        // Arrange
        var thread = BookThreadFactory.validThread();
        var response = BookThreadResponseFactory.validResponse(thread);

        doNothing()
                .when(messagingTemplate)
                .convertAndSend(any(String.class), eq(response));

        // Act && Assert
        assertDoesNotThrow(() -> bookThreadWebSocketNotificationObserver.notify(response));
        verify(messagingTemplate, times(1)).convertAndSend(any(String.class), eq(response));
    }

}
