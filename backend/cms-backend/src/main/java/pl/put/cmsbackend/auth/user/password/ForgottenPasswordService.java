package pl.put.cmsbackend.auth.user.password;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.notification.NotificationRequest;
import pl.put.cmsbackend.notification.NotificationService;
import pl.put.cmsbackend.notification.NotificationType;
import pl.put.cmsbackend.notification.email.EmailNotification;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ForgottenPasswordService {

    private final NotificationService notificationService;

    public void handleForgottenPassword(String email) {
        EmailNotification forgottenPasswordNotification = EmailNotification.builder()
                .htmlTemplate("ForgottenPassword.html")
                .recipient(email)
                .subject("Forgotten Password")
                .templateVariables(Map.of("username", email))
                .build();


        NotificationRequest notificationRequest = NotificationRequest.builder()
                .notificationType(NotificationType.EMAIL)
                .notification(forgottenPasswordNotification)
                .build();

        notificationService.sendNotification(notificationRequest);
    }

}
