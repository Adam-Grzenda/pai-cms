package pl.put.cmsbackend.notification.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.notification.NotificationRequest;
import pl.put.cmsbackend.notification.NotificationService;
import pl.put.cmsbackend.notification.NotificationType;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(NotificationRequest request) {
        if (request.getNotificationType() == NotificationType.EMAIL && request.getNotification() instanceof EmailNotification emailNotification) {
            sendEmailNotification(emailNotification);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void sendEmailNotification(EmailNotification notification) {

    }
}
