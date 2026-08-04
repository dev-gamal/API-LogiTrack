package com.logitrack.mapper;

import com.logitrack.dto.request.ClientRequestDTO;
import com.logitrack.dto.response.ClientResponseDTO;
import com.logitrack.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientRequestDTO dto);
    ClientResponseDTO toResponseDTO(Client entity);
    void updateEntity(ClientRequestDTO dto, @MappingTarget Client entity);
}
