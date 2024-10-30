package com.example.user_management_ms.services;

import com.example.user_management_ms.config.RabbitMQConfig;
import com.example.user_management_ms.dto.UserCreateRequestDto;
import com.example.user_management_ms.dto.UserCreateResponseDto;
import com.example.user_management_ms.dto.UserMessageDto;
import com.example.user_management_ms.dto.UserUpdatePasswordRequestDto;
import com.example.user_management_ms.entities.Address;
import com.example.user_management_ms.entities.User;
import com.example.user_management_ms.exceptions.InvalidNewPasswordException;
import com.example.user_management_ms.feign.AddressFeign;
import com.example.user_management_ms.mapper.UserMapper;
import com.example.user_management_ms.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private UserRepository userRepository;
    private final AddressFeign addressFeign;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user.exchange}")
    private String exchange;

    public UserService(UserRepository userRepository, @Value("${rabbitmq.user.exchange}") String exchange, RabbitTemplate rabbitTemplate, AddressFeign addressFeign) {
        this.userRepository = userRepository;
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
        this.addressFeign = addressFeign;
    }

    public UserCreateResponseDto create(UserCreateRequestDto userRequestDto){
        User user = UserMapper.INSTANCE.convertDtoToEntity(userRequestDto);

        Address address = addressFeign.findAddressByCep(user.getCep());
        user.setAddress(address);

        userRepository.save(user);
        notify(user, exchange, RabbitMQConfig.USER_CREATED_ROUTING_KEY);

        return UserMapper.INSTANCE.convertEntityToDto(user);
    }

    public void updatePassword(UserUpdatePasswordRequestDto passwordRequestDto){
        User user = findUserByUsername(passwordRequestDto.getUsername());
        if (!Objects.equals(user.getPassword(), passwordRequestDto.getNewPassword())) {
            userRepository.updatePassword(user.getId(), passwordRequestDto.getNewPassword());
            notify(user, exchange, RabbitMQConfig.USER_UPDATED_ROUTING_KEY);
        } else {
            throw new InvalidNewPasswordException();
        }
    }

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public void notify(User user, String exchange, String routingKey){
        UserMessageDto userMessageDto = UserMapper.INSTANCE.convertEntityToMessageDto(user);
        userMessageDto.setOperation(routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, userMessageDto);
    }
}
