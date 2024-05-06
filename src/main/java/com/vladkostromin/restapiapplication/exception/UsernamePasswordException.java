package com.vladkostromin.restapiapplication.exception;

public class UsernamePasswordException extends ApiException {

    public UsernamePasswordException(String message, String errorCode) {
        super(message, errorCode);
    }
}
