package com.logitrack.mapper;

import com.logitrack.dto.response.UserResponseDTO;
import com.logitrack.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(entity.getRole().name())")
    UserResponseDTO toResponseDTO(User entity);
}
