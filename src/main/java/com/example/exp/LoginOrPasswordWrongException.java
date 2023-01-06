package com.example.exp;

public class LoginOrPasswordWrongException extends RuntimeException{
    public LoginOrPasswordWrongException(String message) {
        super(message);
    }
}
