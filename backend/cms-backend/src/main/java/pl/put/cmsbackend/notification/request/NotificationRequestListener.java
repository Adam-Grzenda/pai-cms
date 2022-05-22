package pl.put.cmsbackend.notification.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.put.cmsbackend.notification.NotificationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequestListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void onNotificationRequest(NotificationRequest request) {
        log.debug("Received notification request: {}", request.toString());
        notificationService.sendNotification(request);
    }
}
