package com.example.youtube.exp;

public class VideoNotPublicException extends RuntimeException {
    public VideoNotPublicException(String message) {
        super(message);
    }
}
