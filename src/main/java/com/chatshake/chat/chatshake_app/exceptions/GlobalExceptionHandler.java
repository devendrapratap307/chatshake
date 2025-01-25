package com.chatshake.chat.chatshake_app.exceptions;

import com.chatshake.chat.chatshake_app.dto.ResponseTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseTO resp;
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
//        resp = ResponseTO.buildWithMsg(HttpStatus.NOT_FOUND.value(),"EX001", request.getRequestURI(), "Entity Not Found","exception", null);
//        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        resp = ResponseTO.buildWithMsg(HttpStatus.BAD_REQUEST.value(),"EX001", request.getRequestURI(), ex.getMessage(),"exception", errorMessages);
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
        resp = ResponseTO.buildWithMsg(HttpStatus.INTERNAL_SERVER_ERROR.value(),"EX001", request.getRequestURI(), ex.getMessage(),"exception", null);
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

