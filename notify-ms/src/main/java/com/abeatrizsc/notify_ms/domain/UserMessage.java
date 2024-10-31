package com.abeatrizsc.notify_ms.domain;

import com.abeatrizsc.notify_ms.enums.UserOperationsEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    private String username;
    private UserOperationsEnum operation;
}
