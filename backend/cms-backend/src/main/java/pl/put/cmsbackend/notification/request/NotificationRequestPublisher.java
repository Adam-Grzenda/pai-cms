package pl.put.cmsbackend.notification.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequestPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishRequest(NotificationRequest request) {
        log.debug("Publishing notification request: {}", request.toString());
        applicationEventPublisher.publishEvent(request);
    }

}
