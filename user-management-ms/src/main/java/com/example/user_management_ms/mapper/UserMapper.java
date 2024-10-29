package com.example.user_management_ms.mapper;

import com.example.user_management_ms.dto.UserCreateRequestDto;
import com.example.user_management_ms.dto.UserCreateResponseDto;
import com.example.user_management_ms.entities.Address;
import com.example.user_management_ms.entities.User;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = Address.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User convertDtoToEntity(UserCreateRequestDto requestDto);

    UserCreateResponseDto convertEntityToDto(User user);
}
