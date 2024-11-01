package com.abeatrizsc.notify_ms.entity;

import com.abeatrizsc.notify_ms.enums.UserOperationsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users_messages")
public class UserMessage implements Serializable {
    @Id
    private String id;
    private String username;
    private UserOperationsEnum operation;
}
