package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookActivityWebSocketNotificationObserver implements BaseBookActivityNotificationObserver {

    private final UserSocialService userSocialService;
    private final SimpMessagingTemplate messagingTemplate;

    public void notify(BookActivityResponse response) {
        notifyUserProfile(response);
        notifyUserFollowersFeed(response);
    }

    private void notifyUserProfile(BookActivityResponse response) {
        var destination = String.format("/topic/users/%s", response.getUser().getUsername());
        messagingTemplate.convertAndSend(destination, response);
    }

    private void notifyUserFollowersFeed(BookActivityResponse response) {
        userSocialService
                .findFollowers(response.getUser().getUsername())
                .forEach(follower -> {
                    var destination = String.format("/topic/users/%s/social/feed", follower.getUsername());
                    messagingTemplate.convertAndSend(destination, response);
                });
    }

}
