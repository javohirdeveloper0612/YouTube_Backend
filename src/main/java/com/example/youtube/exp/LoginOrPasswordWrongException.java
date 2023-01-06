package com.example.youtube.exp;

public class LoginOrPasswordWrongException extends RuntimeException {
    public LoginOrPasswordWrongException(String message) {
        super(message);
    }
}
