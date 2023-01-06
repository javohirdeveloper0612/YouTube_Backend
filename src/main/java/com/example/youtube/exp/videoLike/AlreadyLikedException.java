package com.example.youtube.exp.videoLike;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException(String massage) {
        super(massage);
    }
}
