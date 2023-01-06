package com.example.youtube.exp.playlist;

public class PlaylistNoAccessException extends RuntimeException {

    public PlaylistNoAccessException(String message) {
        super(message);
    }
}
