package com.abeatrizsc.notify_ms.dto;

import com.abeatrizsc.notify_ms.entity.UserMessage;
import com.abeatrizsc.notify_ms.enums.UserOperationsEnum;
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

    public UserMessage toEntity() {
        UserMessage userMessage = new UserMessage();
        userMessage.setUsername(this.username);
        userMessage.setOperation(this.operation);
        return userMessage;
    }
}
