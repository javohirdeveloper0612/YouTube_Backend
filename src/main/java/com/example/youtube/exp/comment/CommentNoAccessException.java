package com.example.youtube.exp.comment;

public class CommentNoAccessException extends RuntimeException {
    public CommentNoAccessException(String message) {
        super(message);
    }
}
