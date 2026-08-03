package com.logitrack.services;

import com.logitrack.entities.*;
import com.logitrack.repositories.ClientRepository;
import com.logitrack.repositories.OrderLineRepository;
import com.logitrack.repositories.OrderRepository;
import com.logitrack.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderLineRepository orderLineRepository;

    @Transactional
    public Order createOrder(int clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Order order = new Order();
        order.setClient(client);
        order.setStatut(OrderStatus.PENDING);

        return orderRepository.save(order);
    }


    // OrderLineService
    @Transactional
    public OrderLine addProductToOrder(int orderId, int productId, int quantite) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (product.getStockAmount() < quantite) {
            throw new RuntimeException("Stock insuffisant pour le produit : " + product.getName());
        }

        product.setStockAmount(product.getStockAmount() - quantite);
        productRepository.save(product);

        OrderLine orderLine = new OrderLine();
        orderLine.setOrder(order);
        orderLine.setProduct(product);
        orderLine.setQuantite(quantite);

        return orderLineRepository.save(orderLine);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    @Transactional
    public Order updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        order.setStatut(newStatus);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByClientId(int clientId) {
        return orderRepository.findByClientId(clientId);
    }

    public int getTotalOrdersCount() {
        return orderRepository.countTotalOrders();
    }

    public Product getTopSellingProduct() {
        return productRepository.findTopProduct();
    }

}