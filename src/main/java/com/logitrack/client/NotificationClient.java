package com.logitrack.client;

import com.logitrack.dto.request.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notification-service",
        url = "${NOTIFICATION_SERVICE_URL:http://localhost:8081}",
        fallback = NotificationClientFallback.class)
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationRequestDTO request);
}