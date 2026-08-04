package com.logitrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Integer id;
    private LocalDateTime orderDate;
    private String statut;
    private Integer clientId;
    private String clientName;
    private List<OrderLineResponseDTO> orderLines;
}
