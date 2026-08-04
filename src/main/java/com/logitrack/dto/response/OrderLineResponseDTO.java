package com.logitrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineResponseDTO {
    private Integer id;
    private Integer quantite;
    private Integer productId;
    private String productName;
    private Double productPrice;
}
