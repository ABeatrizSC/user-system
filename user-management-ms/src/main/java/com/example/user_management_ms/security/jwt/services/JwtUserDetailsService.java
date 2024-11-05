package com.example.user_management_ms.security.jwt.services;

import com.example.user_management_ms.entities.User;
import com.example.user_management_ms.exceptions.UserNotFoundException;
import com.example.user_management_ms.security.jwt.JwtUserDetails;
import com.example.user_management_ms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return new JwtUserDetails(user);
    }
}
