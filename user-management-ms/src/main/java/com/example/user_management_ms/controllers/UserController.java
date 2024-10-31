package com.example.user_management_ms.controllers;

import com.example.user_management_ms.dto.UserCreateRequestDto;
import com.example.user_management_ms.dto.UserCreateResponseDto;
import com.example.user_management_ms.dto.UserUpdatePasswordRequestDto;
import com.example.user_management_ms.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Void> updatePassword(@RequestBody UserUpdatePasswordRequestDto passwordRequestDto, HttpServletRequest request){
        userService.updatePassword(passwordRequestDto, request);
        return ResponseEntity.noContent().build();
    }
}
