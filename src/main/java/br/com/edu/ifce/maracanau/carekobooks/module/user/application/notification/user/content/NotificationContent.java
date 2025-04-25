package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationContent {

    private String title;
    private String message;
    private String messageHtml;

}
