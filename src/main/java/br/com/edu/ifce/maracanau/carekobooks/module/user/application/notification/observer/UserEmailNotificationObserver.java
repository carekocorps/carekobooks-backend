package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.AuthUserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.auth.AuthEmailNotificationException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.content.NotificationContent;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserEmailNotificationObserver implements BaseUserNotificationObserver {

    private final JavaMailSender mailSender;

    @Override
    public void notify(AuthUserResponse response, NotificationContent content) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setTo(response.getEmail());
            helper.setSubject(content.getTitle());
            helper.setText(content.getMessageHtml(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new AuthEmailNotificationException("Error sending email to " + response.getUsername());
        }
    }

}
