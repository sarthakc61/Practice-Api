package com.api.practice.exceptions;

public class UserNotFoundException extends RuntimeException{

    public String errorMessage;
    public Integer errorCode;

    public UserNotFoundException(){
        super();
    }
    public UserNotFoundException(String message){
        super(message);
    }

}
