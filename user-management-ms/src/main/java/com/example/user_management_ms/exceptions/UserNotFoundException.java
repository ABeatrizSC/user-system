package com.example.user_management_ms.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    public UserNotFoundException(String username) {
        super(String.format("User '%s' not found.", username));
    }
}
