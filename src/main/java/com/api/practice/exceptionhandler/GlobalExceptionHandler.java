package com.api.practice.exceptionhandler;

import com.api.practice.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundException> userExceptionHandler(UserNotFoundException userNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFoundException);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class , ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> errorHandling(Exception ex){
        Map<String, String> map = new HashMap<>();
        if(ex instanceof MethodArgumentNotValidException){
            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()
                    .forEach(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        if(ex instanceof ConstraintViolationException){
            ((ConstraintViolationException) ex).getConstraintViolations()
                    .forEach(constraintViolation -> map.put(constraintViolation.getConstraintDescriptor().getMessageTemplate(), constraintViolation.getMessage()));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
