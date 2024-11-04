package com.example.user_management_ms.services;

import com.example.user_management_ms.config.RabbitMQConfig;
import com.example.user_management_ms.config.SecurityConfig;
import com.example.user_management_ms.dto.UserCreateRequestDto;
import com.example.user_management_ms.dto.UserCreateResponseDto;
import com.example.user_management_ms.dto.UserMessageDto;
import com.example.user_management_ms.dto.UserUpdatePasswordRequestDto;
import com.example.user_management_ms.dto.security.AccountCredentialsDto;
import com.example.user_management_ms.entities.Address;
import com.example.user_management_ms.entities.User;
import com.example.user_management_ms.enums.UserOperationsEnum;
import com.example.user_management_ms.exceptions.InvalidNewPasswordException;
import com.example.user_management_ms.exceptions.UserNotFoundException;
import com.example.user_management_ms.feign.AddressFeign;
import com.example.user_management_ms.mapper.UserMapper;
import com.example.user_management_ms.repositories.UserRepository;
import com.example.user_management_ms.security.jwt.JwtToken;
import com.example.user_management_ms.security.jwt.JwtUserDetails;
import com.example.user_management_ms.security.jwt.services.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;
    private SecurityConfig securityConfig;
    private final AddressFeign addressFeign;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user.exchange}")
    private String exchange;

    public UserService(@Value("${rabbitmq.user.exchange}") String exchange, RabbitTemplate rabbitTemplate, AddressFeign addressFeign, SecurityConfig securityConfig, JwtTokenService jwtTokenService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
        this.addressFeign = addressFeign;
        this.securityConfig = securityConfig;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public UserCreateResponseDto create(UserCreateRequestDto userRequestDto){
        User user = UserMapper.INSTANCE.convertDtoToEntity(userRequestDto);

        Address address = addressFeign.findAddressByCep(user.getCep());
        user.setAddress(address);

        String encryptedNewPassword = encodePassword(user.getPassword());
        user.setPassword(encryptedNewPassword);

        userRepository.save(user);
        notify(user, exchange, RabbitMQConfig.USER_CREATED_ROUTING_KEY);

        return UserMapper.INSTANCE.convertEntityToDto(user);
    }

    public void updatePassword(UserUpdatePasswordRequestDto passwordRequestDto, HttpServletRequest request){
        String tokenUsername = jwtTokenService.getSubjectFromToken(jwtTokenService.recoveryToken(request));

        User user = findUserByUsername(passwordRequestDto.getUsername());

        if (!tokenUsername.equals(passwordRequestDto.getUsername())) {
            throw new SecurityException("Unauthorized action.");
        }

        if (arePasswordsDifferent(passwordRequestDto, user) && isCurrentPasswordCorrect(passwordRequestDto, user)) {
            String encryptedNewPassword = encodePassword(passwordRequestDto.getNewPassword());
            userRepository.updatePassword(user.getId(), encryptedNewPassword);

            notify(user, exchange, RabbitMQConfig.USER_UPDATED_ROUTING_KEY);
        }
    }

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void notify(User user, String exchange, UserOperationsEnum operation){
        UserMessageDto userMessageDto = UserMapper.INSTANCE.convertEntityToMessageDto(user);
        userMessageDto.setOperation(operation);
        rabbitTemplate.convertAndSend(exchange, String.valueOf(operation), userMessageDto);
    }

    public JwtToken authenticateUser(AccountCredentialsDto credentialsDto) {
        findUserByUsername(credentialsDto.getUsername());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(credentialsDto.getUsername(), credentialsDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();

        return new JwtToken(jwtTokenService.generateToken(userDetails));
    }

    public String encodePassword(String password){
        return securityConfig.passwordEncoder().encode(password);
    }

    public Boolean arePasswordsDifferent(UserUpdatePasswordRequestDto passwordRequestDto, User user){
        if (!securityConfig.passwordEncoder().matches(passwordRequestDto.getNewPassword(), user.getPassword())){
            return true;
        } else {
            throw new InvalidNewPasswordException();
        }
    }

    public Boolean isCurrentPasswordCorrect(UserUpdatePasswordRequestDto passwordRequestDto, User user){
        if (securityConfig.passwordEncoder().matches(passwordRequestDto.getOldPassword(), user.getPassword())) {
            return true;
        } else {
            throw new SecurityException("Current password is incorrect.");
        }
    }
}
