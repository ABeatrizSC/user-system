package com.example.user_management_ms.controllers;

import com.example.user_management_ms.dto.UserCreateRequestDto;
import com.example.user_management_ms.dto.UserCreateResponseDto;
import com.example.user_management_ms.dto.UserUpdatePasswordRequestDto;
import com.example.user_management_ms.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserCreateResponseDto> create(@RequestBody UserCreateRequestDto userRequest){
        UserCreateResponseDto userResponse = userService.create(userRequest);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(userResponse.getUsername())
                .toUri()).body(userResponse);
    }

    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody UserUpdatePasswordRequestDto passwordRequestDto){
        userService.updatePassword(passwordRequestDto);
        return ResponseEntity.noContent().build();
    }
}