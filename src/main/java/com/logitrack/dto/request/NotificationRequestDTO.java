package com.logitrack.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequestDTO {
    private String message;
    private String type;
    private Long orderId;
}
