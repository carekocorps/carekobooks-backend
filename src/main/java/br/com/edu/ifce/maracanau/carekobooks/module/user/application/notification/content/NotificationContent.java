package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.content;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NotificationContent {

    private String title;
    private String message;
    private String messageHtml;

    public static NotificationContent fromVerificationToken(UUID verificationToken) {
        var title = "Verificação de Conta";
        var message = "Use o token a seguir para verificar sua conta do Carekobooks: " + verificationToken;
        var messageHtml = """
            <html>
                <body style="font-family: Arial, sans-serif;">
                    <div style="background-color: #f5f5f5; padding: 20px;">
                        <h2 style="color: #333;">Bem-vindo ao Carekobooks!</h2>
                        <p style="font-size: 16px;">Por favor, insira o token de verificação abaixo para continuar:</p>
                        <div style="background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                            <h3 style="color: #333;">Token de Verificação:</h3>
                            <p style="font-size: 18px; font-weight: bold; color: #007bff;">%s</p>
                        </div>
                        <p style="font-size: 16px;">Venha ser um Carekobooker com a gente e explore o mundo dos livros!</p>
                    </div>
                </body>
            </html>
            """.formatted(verificationToken);

        var content = new NotificationContent();
        content.setTitle(title);
        content.setMessage(message);
        content.setMessageHtml(messageHtml);
        return content;
    }

    public static NotificationContent fromResetToken(UUID resetToken) {
        var title = "Redefinição de Senha";
        var message = "Use o token a seguir para redefinir sua senha do Carekobooks: " + resetToken;
        var messageHtml = """
            <html>
                <body style="font-family: Arial, sans-serif;">
                    <div style="background-color: #f5f5f5; padding: 20px;">
                        <h2 style="color: #333;">Solicitação de Redefinição de Senha</h2>
                        <p style="font-size: 16px;">Recebemos uma solicitação para redefinir sua senha. Use o token abaixo:</p>
                        <div style="background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                            <h3 style="color: #333;">Token de Redefinição:</h3>
                            <p style="font-size: 18px; font-weight: bold; color: #dc3545;">%s</p>
                        </div>
                        <p style="font-size: 16px;">Se você não solicitou essa alteração, ignore este e-mail.</p>
                    </div>
                </body>
            </html>
            """.formatted(resetToken);

        var content = new NotificationContent();
        content.setTitle(title);
        content.setMessage(message);
        content.setMessageHtml(messageHtml);
        return content;
    }

}
