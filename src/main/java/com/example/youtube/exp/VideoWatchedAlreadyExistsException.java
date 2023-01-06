package com.example.youtube.exp;

public class VideoWatchedAlreadyExistsException extends RuntimeException {
    public VideoWatchedAlreadyExistsException(String message) {
        super(message);
    }
}
