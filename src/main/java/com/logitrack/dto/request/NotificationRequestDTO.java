package com.logitrack.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequestDTO {
    @NotNull(message = "Message is required")
    private String message;

    private String type;

    @NotNull(message = "Order ID is required")
    private Long orderId;
}
