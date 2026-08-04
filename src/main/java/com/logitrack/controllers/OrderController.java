package com.logitrack.controllers;

import com.logitrack.dto.request.OrderLineRequestDTO;
import com.logitrack.dto.response.OrderLineResponseDTO;
import com.logitrack.dto.response.OrderResponseDTO;
import com.logitrack.dto.response.ProductResponseDTO;
import com.logitrack.entities.OrderStatus;
import com.logitrack.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestParam int clientId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(clientId));
    }

    @PostMapping("/{orderId}/products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<OrderLineResponseDTO> addProductToOrder(
            @PathVariable int orderId,
            @Valid @RequestBody OrderLineRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addProductToOrder(orderId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable int id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable int id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        // For now, just return no content — can add service method later
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByClient(@PathVariable int clientId) {
        return ResponseEntity.ok(orderService.getOrdersByClientId(clientId));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Integer> getTotalOrdersCount() {
        return ResponseEntity.ok(orderService.getTotalOrdersCount());
    }

    @GetMapping("/top-product")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductResponseDTO> getTopSellingProduct() {
        ProductResponseDTO topProduct = orderService.getTopSellingProduct();
        if (topProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topProduct);
    }
}