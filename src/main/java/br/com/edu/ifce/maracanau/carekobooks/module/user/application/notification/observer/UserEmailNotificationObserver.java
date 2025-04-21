package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.observer;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.content.NotificationContent;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
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
    public void notify(User user, NotificationContent content) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setTo(user.getEmail());
            helper.setSubject(content.getTitle());
            helper.setText(content.getMessageHtml(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BadRequestException("Error sending email to " + user.getUsername());
        }
    }

}
