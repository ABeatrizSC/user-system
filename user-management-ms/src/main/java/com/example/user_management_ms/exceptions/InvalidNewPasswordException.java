package com.example.user_management_ms.exceptions;

public class InvalidNewPasswordException extends RuntimeException {
    public InvalidNewPasswordException() {
        super("The new password can't be the same as the old password.");
    }
}
