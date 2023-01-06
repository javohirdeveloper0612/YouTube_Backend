package com.example.youtube.controller;

import com.example.youtube.exp.*;
import com.example.youtube.exp.channel.ChannelAccessDeniedException;
import com.example.youtube.exp.channel.ChannelNotExistsException;
import com.example.youtube.exp.comment.CommentNoAccessException;
import com.example.youtube.exp.comment.CommentNotFoundException;
import com.example.youtube.exp.playlist.PlaylistNoAccessException;
import com.example.youtube.exp.playlist.PlaylistNotFoundException;
import com.example.youtube.exp.tag.TagNotFound;
import com.example.youtube.exp.videoLike.AlreadyLikedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = new LinkedList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({EmailAlreadyExistsException.class})
    private ResponseEntity<?> handler(EmailAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({IncorrectDateFormatException.class})
    private ResponseEntity<?> handler(IncorrectDateFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({LimitOutPutException.class})
    private ResponseEntity<?> handler(LimitOutPutException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({LoginOrPasswordWrongException.class})
    private ResponseEntity<?> handler(LoginOrPasswordWrongException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({StatusBlockException.class})
    private ResponseEntity<?> handler(StatusBlockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({CouldNotRead.class})
    private ResponseEntity<?> handler(CouldNotRead e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({FileNotFoundException.class})
    private ResponseEntity<?> handler(FileNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({FileUploadException.class})
    private ResponseEntity<?> handler(FileUploadException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({OriginalFileNameNullException.class})
    private ResponseEntity<?> handler(OriginalFileNameNullException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({SomethingWentWrong.class})
    private ResponseEntity<?> handler(SomethingWentWrong e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({ProfileNotFoundException.class})
    private ResponseEntity<?> handler(ProfileNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({ChannelNotExistsException.class})
    private ResponseEntity<?> handler(ChannelNotExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({ChannelAccessDeniedException.class})
    private ResponseEntity<?> handler(ChannelAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({TagNotFound.class})
    private ResponseEntity<?> handler(TagNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler({AlreadyLikedException.class})
    private ResponseEntity<?> handler(AlreadyLikedException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({VideoNotFoundException.class})
    private ResponseEntity<?> handler(VideoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({VideoOwnerException.class})
    private ResponseEntity<?> handler(VideoOwnerException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({VideoNotPublicException.class})
    private ResponseEntity<?> handler(VideoNotPublicException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({PlaylistNotFoundException.class})
    private ResponseEntity<?> handler(PlaylistNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({PlaylistNoAccessException.class})
    private ResponseEntity<?> handler(PlaylistNoAccessException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({CommentNoAccessException.class})
    private ResponseEntity<?> handler(CommentNoAccessException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({CommentNotFoundException.class})
    private ResponseEntity<?> handler(CommentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
