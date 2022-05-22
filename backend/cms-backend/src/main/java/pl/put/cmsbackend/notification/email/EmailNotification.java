package pl.put.cmsbackend.notification.email;

import lombok.Builder;
import lombok.Getter;
import pl.put.cmsbackend.notification.Notification;

import java.util.Map;

@Builder
@Getter
public class EmailNotification extends Notification {

    private String recipient;
    private String subject;
    private String htmlTemplate;
    private Map<String, String> templateVariables;
}
