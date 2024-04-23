package com.vladkostromin.restapiapplication.exception;

public class IncorrectUsernamePasswordException extends ApiException {

    public IncorrectUsernamePasswordException(String message, String errorCode) {
        super(message, errorCode);
    }
}
