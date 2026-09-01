package com.logitrack.services;

import com.logitrack.client.NotificationClient;
import com.logitrack.dto.request.NotificationRequestDTO;
import com.logitrack.dto.request.OrderLineRequestDTO;
import com.logitrack.dto.response.OrderLineResponseDTO;
import com.logitrack.dto.response.OrderResponseDTO;
import com.logitrack.dto.response.ProductResponseDTO;
import com.logitrack.entities.*;
import com.logitrack.exception.ResourceNotFoundException;
import com.logitrack.mapper.OrderLineMapper;
import com.logitrack.mapper.OrderMapper;
import com.logitrack.mapper.ProductMapper;
import com.logitrack.repositories.ClientRepository;
import com.logitrack.repositories.OrderLineRepository;
import com.logitrack.repositories.OrderRepository;
import com.logitrack.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderMapper orderMapper;
    private final OrderLineMapper orderLineMapper;
    private final ProductMapper productMapper;
    private final NotificationClient notificationClient;

    @Transactional
    public OrderResponseDTO createOrder(int clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID : " + clientId));

        Order order = new Order();
        order.setClient(client);
        order.setStatut(OrderStatus.PENDING);

        order = orderRepository.save(order);

        sendNotification("The order #" + order.getId() + " has been created successfully.", "ORDER_CREATED", order.getId());

        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderLineResponseDTO addProductToOrder(int orderId, OrderLineRequestDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID : " + orderId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID : " + request.getProductId()));

        if (product.getStockAmount() < request.getQuantite()) {
            log.error("Insufficient stock for the product : {}", product.getName());
            throw new IllegalArgumentException("insufficient stock for the product : " + product.getName());
        }

        product.setStockAmount(product.getStockAmount() - request.getQuantite());
        productRepository.save(product);

        OrderLine orderLine = new OrderLine();
        orderLine.setOrder(order);
        orderLine.setProduct(product);
        orderLine.setQuantite(request.getQuantite());

        orderLine = orderLineRepository.save(orderLine);
        return orderLineMapper.toResponseDTO(orderLine);
    }

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable, OrderStatus statut, Integer clientId) {
        Specification<Order> spec = Specification.where(null);

        if (statut != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("statut"), statut));
        }

        if (clientId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("client").get("id"), clientId));
        }

        return orderRepository.findAll(spec, pageable).map(orderMapper::toResponseDTO);
    }

    public OrderResponseDTO getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID : " + id));
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID : " + orderId));
        order.setStatut(newStatus);
        order = orderRepository.save(order);

        if (newStatus == OrderStatus.SHIPPED) {
            sendNotification("The order #" + orderId + " has been shipped.", "ORDER_SHIPPED", orderId);
        } else if (newStatus == OrderStatus.DELIVERED) {
            sendNotification("The order #" + orderId + " has been delivered.", "ORDER_DELIVERED", orderId);
        }

        return orderMapper.toResponseDTO(order);
    }

    public List<OrderResponseDTO> getOrdersByClientId(int clientId) {
        return orderRepository.findByClientId(clientId).stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public int getTotalOrdersCount() {
        return orderRepository.countTotalOrders();
    }

    public ProductResponseDTO getTopSellingProduct() {
        Product topProduct = productRepository.findTopProduct();
        if (topProduct == null) {
            return null;
        }
        return productMapper.toResponseDTO(topProduct);
    }

    private void sendNotification(String message, String type, int orderId) {
        try {
            NotificationRequestDTO request = NotificationRequestDTO.builder()
                    .message(message)
                    .type(type)
                    .orderId((long) orderId)
                    .build();

            notificationClient.sendNotification(request);
            log.info("Notification sent successfully for order {}", orderId);

        } catch (Exception e) {
            log.error("Failed to send notification for order {} : {}", orderId, e.getMessage());
        }
    }
}