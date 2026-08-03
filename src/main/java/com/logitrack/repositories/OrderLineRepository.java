package com.logitrack.repositories;

import com.logitrack.entities.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {

    @Query("select count(ol) from OrderLine ol where ol.product.id = :productId")
    int countOrderLineByProduct(@Param("productId") int productId);
}