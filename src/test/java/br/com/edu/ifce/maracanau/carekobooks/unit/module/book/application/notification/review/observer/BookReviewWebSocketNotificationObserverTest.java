package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.notification.review.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookReviewResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.review.observer.BookReviewWebSocketNotificationObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookReviewWebSocketNotificationObserverTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private BookReviewWebSocketNotificationObserver bookReviewWebSocketNotificationObserver;

    @Test
    void notify_withValidReviewResponse_shouldSucceed() {
        // Arrange
        var review = BookReviewFactory.validReview();
        var response = BookReviewResponseFactory.validResponse(review);

        doNothing()
                .when(messagingTemplate)
                .convertAndSend(any(String.class), eq(response));

        // Act && Assert
        assertDoesNotThrow(() -> bookReviewWebSocketNotificationObserver.notify(response));
        verify(messagingTemplate, times(1)).convertAndSend(any(String.class), eq(response));
    }

}
