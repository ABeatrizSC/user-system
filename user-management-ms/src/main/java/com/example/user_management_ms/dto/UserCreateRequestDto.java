package com.example.user_management_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateRequestDto {
    private String username;
    private String password;
    private String email;
    private String cep;
}
