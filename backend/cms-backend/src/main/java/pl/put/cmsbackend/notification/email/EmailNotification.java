package pl.put.cmsbackend.notification.email;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import pl.put.cmsbackend.notification.Notification;

import java.util.Map;

@Builder
@Getter
@ToString
public class EmailNotification extends Notification {

    private String recipient;
    private String subject;
    private String htmlTemplate;
    @ToString.Exclude
    private Map<String, String> templateVariables;
}
