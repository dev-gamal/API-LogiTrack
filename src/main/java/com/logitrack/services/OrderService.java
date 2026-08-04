package com.logitrack.services;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderMapper orderMapper;
    private final OrderLineMapper orderLineMapper;
    private final ProductMapper productMapper;

    @Transactional
    public OrderResponseDTO createOrder(int clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID : " + clientId));

        Order order = new Order();
        order.setClient(client);
        order.setStatut(OrderStatus.PENDING);

        order = orderRepository.save(order);
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderLineResponseDTO addProductToOrder(int orderId, OrderLineRequestDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + orderId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + request.getProductId()));

        if (product.getStockAmount() < request.getQuantite()) {
            throw new IllegalArgumentException("Stock insuffisant pour le produit : " + product.getName());
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

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toResponseDTO);
    }

    public OrderResponseDTO getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + id));
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + orderId));
        order.setStatut(newStatus);
        order = orderRepository.save(order);
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
}