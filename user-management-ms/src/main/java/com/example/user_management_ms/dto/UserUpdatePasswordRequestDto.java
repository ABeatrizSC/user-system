package com.example.user_management_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdatePasswordRequestDto {
    private String username;
    private String oldPassword;
    private String newPassword;
}
