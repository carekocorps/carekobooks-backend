package br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.factory;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.notification.user.content.NotificationContent;

public class NotificationContentFactory {

    private NotificationContentFactory() {
    }

    public static NotificationContent buildFromRegistrationOtp(String otp) {
        var title = "Verificação de Conta";
        var message = "Use o token a seguir para verificar sua conta do Carekobooks: " + otp;
        var messageHtml = generateMessageHtml(title, "Por favor, insira o token abaixo para completar seu cadastro:", otp, "Venha ser um Carekobooker com a gente e explore o mundo dos livros!");
        return new NotificationContent(title, message, messageHtml);
    }

    public static NotificationContent buildFromPasswordOtp(String otp) {
        var title = "Redefinição de Senha";
        var message = "Use o token a seguir para redefinir sua senha do Carekobooks: " + otp;
        var messageHtml = generateMessageHtml(title, "Recebemos uma solicitação para redefinir sua senha. Use o token abaixo:", otp, "Se você não solicitou essa alteração, ignore este e-mail.");
        return new NotificationContent(title, message, messageHtml);
    }

    public static NotificationContent buildFromEmailOtp(String otp) {
        var title = "Confirmação de Alteração de E-mail";
        var message = "Use o token a seguir para confirmar a alteração de e-mail da sua conta do Carekobooks: " + otp;
        var messageHtml = generateMessageHtml(title, "Recebemos uma solicitação para alterar o e-mail da sua conta. Use o token abaixo para confirmar:", otp, "Se você não solicitou essa alteração, ignore este e-mail.");
        return new NotificationContent(title, message, messageHtml);
    }

    private static String generateMessageHtml(String title, String instructions, String otp, String finalMessage) {
        return """
            <html>
              <body style="margin: 0; padding: 0; font-family: 'Open Sans', 'Roboto', Arial, sans-serif; background-color: #f5f7fa;">
                <main role="main" style="max-width: 600px; margin: 20px auto; padding: 20px;">
                  <article style="background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08); overflow: hidden;">
                    <header style="background: linear-gradient(to right, #0a2543, #00438a); padding: 30px 20px; text-align: center;">
                      <h1 style="color: #ffffff; margin: 0; font-size: 26px; font-weight: 700; letter-spacing: 0.5px;">Carekobooks</h1>
                      <p style="color: rgba(255,255,255,0.9); font-size: 16px; margin: 8px 0 0; font-weight: 400;">Bem-vindo à nossa comunidade literária</p>
                    </header>
                    <section style="padding: 32px 30px;">
                      <h2 style="color: #0a2543; font-size: 20px; font-weight: 600; text-align: center; margin: 0 0 16px 0;">%s</h2>
                      <p style="color: #4a5568; font-size: 15px; line-height: 1.6; margin-bottom: 28px; text-align: center;">%s</p>
                      <div role="region" aria-label="Token"
                           style="background-color: #f8fafc;
                                  padding: 24px;
                                  border-radius: 8px;
                                  border: 1px solid #e2e8f0;
                                  margin: 0 auto 32px;
                                  max-width: 100%%;
                                  text-align: center;
                                  box-shadow: inset 0 1px 3px rgba(0,0,0,0.05);">
                        <p style="color: #4a5568; font-size: 14px; font-weight: 600; margin: 0 0 8px 0;">Seu código de verificação:</p>
                        <p style="font-size: 28px;
                                 font-weight: 700;
                                 color: #00438a;
                                 letter-spacing: 2px;
                                 margin: 0;
                                 padding: 12px 0;
                                 background-color: #f0f4f8;
                                 border-radius: 6px;
                                 font-family: 'Roboto', monospace;">%s</p>
                      </div>
                      <p style="color: #4a5568; font-size: 15px; line-height: 1.6; text-align: center; margin-bottom: 0;">%s</p>
                    </section>
                    <footer style="background-color: #f8fafc; padding: 24px; text-align: center; border-top: 1px solid #e2e8f0;">
                      <h3 style="color: #00438a; margin: 0 0 8px 0; font-size: 18px; font-weight: 600;">Leia, Registre & Compartilhe</h3>
                      <p style="color: #718096; font-size: 13px; margin: 0; line-height: 1.5;">
                        © 2025 Carekobooks · Todos os direitos reservados<br>
                        <span style="color: #a0aec0;">Uma comunidade para amantes de livros</span>
                      </p>
                    </footer>
                  </article>
                </main>
              </body>
            </html>
            """.formatted(title, instructions, otp, finalMessage);
    }

}
