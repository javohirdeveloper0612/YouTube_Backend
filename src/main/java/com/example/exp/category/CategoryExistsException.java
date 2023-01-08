package com.example.exp.category;

public class CategoryExistsException extends RuntimeException{
    public CategoryExistsException(String message) {
        super(message);
    }
}
