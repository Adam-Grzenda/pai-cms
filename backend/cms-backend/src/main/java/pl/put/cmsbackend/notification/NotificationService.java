package pl.put.cmsbackend.notification;

import pl.put.cmsbackend.notification.request.NotificationRequest;

public interface NotificationService {
    void sendNotification(NotificationRequest request);
}
