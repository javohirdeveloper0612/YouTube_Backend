package com.example.youtube.exp;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String massage) {
        super(massage);
    }
}
