package com.abeatrizsc.notify_ms.listener;

import com.abeatrizsc.notify_ms.constant.ConstantMessage;
import com.abeatrizsc.notify_ms.domain.UserMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMessageListener {

    @RabbitListener(queues = {"${rabbitmq.queue.user-events.created}", "${rabbitmq.queue.user-events.updated}"})
    public void pendingProposal(UserMessageDto userMessageDto){
        String message = String.format(ConstantMessage.MESSAGE, userMessageDto.getUsername(), userMessageDto.getOperation());
        log.info(message);
    }
}
