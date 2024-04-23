package com.vladkostromin.restapiapplication.exception;

public class TokenExpirationDateException extends ApiException{

    public TokenExpirationDateException(String message, String errorCode) {
        super(message, errorCode);
    }
}
