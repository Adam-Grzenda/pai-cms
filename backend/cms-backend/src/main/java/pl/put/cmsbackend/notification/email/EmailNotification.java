package pl.put.cmsbackend.notification.email;

import pl.put.cmsbackend.notification.Notification;

public class EmailNotification extends Notification {
    private String recipient;
    private String subject;
    private String htmlTemplate;
}
