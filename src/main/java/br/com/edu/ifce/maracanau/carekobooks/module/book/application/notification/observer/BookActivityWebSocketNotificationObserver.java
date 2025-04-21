package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookActivityWebSocketNotificationObserver implements BaseBookActivityNotificationObserver {

    private final BookActivityMapper bookActivityMapper;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public void notify(BookActivity bookActivity) {
        userService
                .findAllFollowersByUsername(bookActivity.getUser().getUsername())
                .forEach(follower -> {
                    var destination = "/topic/users/" + follower.getUsername() + "/feed";
                    messagingTemplate.convertAndSend(destination, bookActivityMapper.toResponse(bookActivity));
                });
    }

}
