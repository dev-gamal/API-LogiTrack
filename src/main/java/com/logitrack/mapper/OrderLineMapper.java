package com.logitrack.mapper;

import com.logitrack.dto.response.OrderLineResponseDTO;
import com.logitrack.entities.OrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderLineMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    OrderLineResponseDTO toResponseDTO(OrderLine entity);
}
