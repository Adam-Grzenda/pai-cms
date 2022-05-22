package pl.put.cmsbackend.notification;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationRequest {

    private NotificationType notificationType;
    private Notification notification;
}
