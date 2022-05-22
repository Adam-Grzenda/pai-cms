package pl.put.cmsbackend.notification.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import pl.put.cmsbackend.notification.Notification;

@Builder
@Getter
@ToString
public class NotificationRequest {

    private NotificationType notificationType;
    private Notification notification;
    public enum NotificationType {
        EMAIL
    }
}
