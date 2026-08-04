package com.logitrack.mapper;

import com.logitrack.dto.response.OrderResponseDTO;
import com.logitrack.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderLineMapper.class})
public interface OrderMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "statut", target = "statut")
    OrderResponseDTO toResponseDTO(Order entity);
}
