package com.example.youtube.exp.channel;

public class ChannelAccessDeniedException extends RuntimeException {
    public ChannelAccessDeniedException(String message) {
        super(message);
    }
}
