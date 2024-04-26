package com.vladkostromin.restapiapplication.exception;

public class InactiveException extends ApiException {


    public InactiveException(String message, String errorCode) {
        super(message, errorCode);
    }
}
