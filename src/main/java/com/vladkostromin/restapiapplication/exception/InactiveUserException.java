package com.vladkostromin.restapiapplication.exception;

public class InactiveUserException extends ApiException {


    public InactiveUserException(String message, String errorCode) {
        super(message, errorCode);
    }
}
