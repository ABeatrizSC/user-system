package com.example.user_management_ms.config;

import com.example.user_management_ms.enums.UserOperationsEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.user.exchange}")
    private String exchange;

    public static final UserOperationsEnum USER_CREATED_ROUTING_KEY = UserOperationsEnum.CREATE;
    public static final UserOperationsEnum USER_UPDATED_ROUTING_KEY = UserOperationsEnum.UPDATE;

    @Bean
    public Queue createQueueNotifyUserCreated(){
        return QueueBuilder.durable("notify.user-events.created").build();
    }

    @Bean
    public Queue createQueueNotifyUserUpdated(){
        return QueueBuilder.durable("notify.user-events.updated").build();
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public DirectExchange createDirectExchange(){
        return ExchangeBuilder.directExchange(exchange).durable(true).build();
    }

    @Bean
    public Binding createBindingNotifyUserCreated(){
        return BindingBuilder.bind(createQueueNotifyUserCreated()).to(createDirectExchange()).with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding createBindingNotifyUserUpdated(){
        return BindingBuilder.bind(createQueueNotifyUserUpdated()).to(createDirectExchange()).with(USER_UPDATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
