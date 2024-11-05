package com.abeatrizsc.notify_ms.listener;

import com.abeatrizsc.notify_ms.constant.ConstantMessage;
import com.abeatrizsc.notify_ms.dto.UserMessageDto;
import com.abeatrizsc.notify_ms.service.UserMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UserMessageListener {
    public UserMessageService service;

    @RabbitListener(queues = {"${rabbitmq.queue.user-events.created}", "${rabbitmq.queue.user-events.updated}"})
    public void pendingProposal(UserMessageDto messageDto){
        String message = String.format(ConstantMessage.MESSAGE, messageDto.getUsername(), messageDto.getOperation());

        try {
            service.save(messageDto.toEntity());
            log.info("Message received and successfully saved to the database: " + message);
        } catch (
                DataAccessException e) {
            log.error("Message received but was occurred a Database error while saving message: " + message, e);
        } catch (Exception e) {
            log.error("Message received but an unexpected error occurred while processing message: " + message, e);
        }
    }
}
