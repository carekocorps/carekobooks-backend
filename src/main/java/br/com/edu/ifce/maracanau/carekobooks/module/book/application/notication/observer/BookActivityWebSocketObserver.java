package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notication.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookActivityWebSocketObserver implements BaseBookActivityObserver {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public void notify(BookActivityResponse response) {
        userService
                .findAllFollowersByUsername(response.getUser().getUsername())
                .forEach(follower -> {
                    var destination = "/topic/users/" + follower.getUsername() + "/feed";
                    messagingTemplate.convertAndSend(destination, response);
                });
    }

}
