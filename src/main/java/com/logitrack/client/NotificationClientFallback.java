package com.logitrack.client;

import com.logitrack.dto.request.NotificationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationClientFallback implements NotificationClient {

    @Override
    public void sendNotification(NotificationRequestDTO request) {
        log.warn("FALLBACK TRIGGERED : Cannot reach the Notification Service. " +
                        "The notification of type {} for the order #{} has been temporarily ignored.",
                request.getType(), request.getOrderId());
    }
}