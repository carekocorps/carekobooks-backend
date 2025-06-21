package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.notification.activity.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookActivityResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.observer.BookActivityWebSocketNotificationObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserSocialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookActivityWebSocketNotificationObserverTest {

    @Mock
    private UserSocialService userSocialService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private BookActivityWebSocketNotificationObserver bookActivityWebSocketNotificationObserver;

    @Test
    void notify_withValidActivityResponseAndValidUerAndValidFollowers() {
        // Arrange
        var userFollowed = UserFactory.validUser();
        var userFollowing = UserFactory.validUserWithFollowing(userFollowed);
        userFollowed.setFollowers(List.of(userFollowing));

        var activity = BookActivityFactory.validActivity(userFollowed);
        var activityResponse = BookActivityResponseFactory.validResponse(activity);

        doNothing()
                .when(messagingTemplate)
                .convertAndSend(any(String.class), eq(activityResponse));

        when(userSocialService.findFollowers(userFollowed.getUsername()))
                .thenReturn(userFollowed.getFollowers().stream().map(SimplifiedUserResponseFactory::validResponse).toList());

        // Act && Assert
        assertDoesNotThrow(() -> bookActivityWebSocketNotificationObserver.notify(activityResponse));
        verify(messagingTemplate, times(userFollowed.getFollowers().size() + 1)).convertAndSend(any(String.class), eq(activityResponse));
        verify(userSocialService, times(1)).findFollowers(userFollowed.getUsername());
    }

}
