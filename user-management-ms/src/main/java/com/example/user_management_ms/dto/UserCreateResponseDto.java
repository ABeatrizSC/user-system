package com.example.user_management_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateResponseDto {
    private String username;
    private String email;
    private AddressResponseDto address;
}
