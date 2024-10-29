package com.example.user_management_ms.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    @ExceptionHandler(InvalidNewPasswordException.class)
    public ResponseEntity<Map<String, String>> invalidNewPasswordException (InvalidNewPasswordException e){
        log.error("API error - ", e);
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleValueAlreadyExistsException (Exception e){
        log.error("API error - ", e);
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Username or email provided has already been registered.");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
