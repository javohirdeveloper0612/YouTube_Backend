package com.example.youtube.exp.channel;

public class ChannelNotExistsException extends RuntimeException {
    public ChannelNotExistsException(String message) {
        super(message);
    }
}
