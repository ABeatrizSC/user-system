package com.abeatrizsc.notify_ms.service;

import com.abeatrizsc.notify_ms.entity.UserMessage;
import com.abeatrizsc.notify_ms.repository.UserMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserMessageService {
    private UserMessageRepository repository;

    public void save(UserMessage message){
        repository.save(message);
    }
}
