package com.example.exp.email;

public class IncorrectDateFormatException extends RuntimeException{
    public IncorrectDateFormatException(String message) {
        super(message);
    }
}
