package pl.put.cmsbackend.notification.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.put.cmsbackend.notification.NotificationRequest;
import pl.put.cmsbackend.notification.NotificationService;
import pl.put.cmsbackend.notification.NotificationType;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendNotification(NotificationRequest request) {
        if (request.getNotificationType() == NotificationType.EMAIL && request.getNotification() instanceof EmailNotification emailNotification) {
            sendEmailNotification(emailNotification);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void sendEmailNotification(EmailNotification notification) {
        String content = prepareContent(notification.getHtmlTemplate(), notification.getTemplateVariables());
        try {
            MimeMessage mimeMessage = createMimeMessage(notification.getRecipient(), notification.getSubject(),
                    content);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error("Error sending mail to: {} with subject {}", notification.getRecipient(),
                    notification.getSubject(), e);
        }


    }

    private MimeMessage createMimeMessage(String recipient, String subject, String content) throws MessagingException {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(recipient);
        helper.setFrom("no-reply@cms-backend.com");
        helper.setSubject(subject);
        helper.setText(content, true);
        return mail;
    }

    private String prepareContent(String template, Map<String, String> templateVariables) {
        Context context = new Context();

        context.setVariables(Collections.unmodifiableMap(templateVariables));
        return templateEngine.process(template, context);
    }
}
