package com.logitrack.services;

import com.logitrack.entities.OrderLine;
import com.logitrack.repositories.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;

    public int getTotalCountOfProduct(int productId) {
        return orderLineRepository.countOrderLineByProduct(productId);
    }
}
