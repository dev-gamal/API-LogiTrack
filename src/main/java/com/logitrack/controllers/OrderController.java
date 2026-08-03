package com.logitrack.controllers;

import com.logitrack.entities.Order;
import com.logitrack.entities.OrderLine;
import com.logitrack.entities.OrderStatus;
import com.logitrack.entities.Product;
import com.logitrack.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam int clientId) {
        Order newOrder = orderService.createOrder(clientId);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/products")
    public ResponseEntity<OrderLine> addProductToOrder(
            @PathVariable int orderId,
            @RequestParam int productId,
            @RequestParam int quantite) {
        OrderLine orderLine = orderService.addProductToOrder(orderId, productId, quantite);
        return new ResponseEntity<>(orderLine, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int  id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable int id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Order>> getOrdersByClient(@PathVariable int clientId) {
        return ResponseEntity.ok(orderService.getOrdersByClientId(clientId));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getTotalOrdersCount() {
        return ResponseEntity.ok(orderService.getTotalOrdersCount());
    }

    @GetMapping("/top-product")
    public ResponseEntity<Product> getTopSellingProduct() {
        Product topProduct = orderService.getTopSellingProduct();
        if (topProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topProduct);
    }
}