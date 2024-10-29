package com.example.user_management_ms.services;

import com.example.user_management_ms.dto.UserCreateRequestDto;
import com.example.user_management_ms.dto.UserCreateResponseDto;
import com.example.user_management_ms.dto.UserUpdatePasswordRequestDto;
import com.example.user_management_ms.entities.Address;
import com.example.user_management_ms.entities.User;
import com.example.user_management_ms.exceptions.InvalidNewPasswordException;
import com.example.user_management_ms.feign.AddressFeign;
import com.example.user_management_ms.mapper.UserMapper;
import com.example.user_management_ms.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;
    private final AddressFeign addressFeign;

    public UserCreateResponseDto create(UserCreateRequestDto userRequestDto){
        User user = UserMapper.INSTANCE.convertDtoToEntity(userRequestDto);

        Address address = addressFeign.findAddressByCep(user.getCep());
        user.setAddress(address);

        userRepository.save(user);

        return UserMapper.INSTANCE.convertEntityToDto(user);
    }

    public void updatePassword(UserUpdatePasswordRequestDto passwordRequestDto){
        User user = findUserByUsername(passwordRequestDto.getUsername());
        if (!Objects.equals(user.getPassword(), passwordRequestDto.getNewPassword())) {
            userRepository.updatePassword(user.getId(), passwordRequestDto.getNewPassword());
        } else {
            throw new InvalidNewPasswordException();
        }
    }

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }
}
