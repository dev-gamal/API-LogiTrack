package com.logitrack.mapper;

import com.logitrack.dto.request.ProductRequestDTO;
import com.logitrack.dto.response.ProductResponseDTO;
import com.logitrack.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDTO dto);
    ProductResponseDTO toResponseDTO(Product entity);
    void updateEntity(ProductRequestDTO dto, @MappingTarget Product entity);
}
