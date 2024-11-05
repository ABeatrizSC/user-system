package com.abeatrizsc.notify_ms.repository;

import com.abeatrizsc.notify_ms.entity.UserMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMessageRepository extends MongoRepository<UserMessage, String> {
}
