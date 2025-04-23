package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookActivityWebSocketNotificationObserver implements BaseBookActivityNotificationObserver {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public void notify(BookActivityResponse response) {
        userService
                .findAllFollowers(response.getUser().getUsername())
                .forEach(follower -> {
                    var destination = "/topic/users/" + follower.getUsername() + "/feed";
                    messagingTemplate.convertAndSend(destination, response);
                });
    }

}
