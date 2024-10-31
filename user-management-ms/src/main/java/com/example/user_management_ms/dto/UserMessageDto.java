package com.example.user_management_ms.dto;

import com.example.user_management_ms.enums.UserOperationsEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageDto {
    private String username;
    private UserOperationsEnum operation;
}
