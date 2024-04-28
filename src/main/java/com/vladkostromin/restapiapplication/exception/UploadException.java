package com.vladkostromin.restapiapplication.exception;

public class UploadException extends ApiException {

    public UploadException(String message, String errorCode) {
        super(message, errorCode);
    }
}
