package com.example.youtube.exp;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String massage) {
        super(massage);
    }
}
