package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;
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
        userSocialService
                .findAllFollowers(response.getUser().getUsername())
                .forEach(follower -> {
                    var destination = "/topic/users/" + follower.getUsername() + "/feed";
                    messagingTemplate.convertAndSend(destination, response);
                });
    }

}
