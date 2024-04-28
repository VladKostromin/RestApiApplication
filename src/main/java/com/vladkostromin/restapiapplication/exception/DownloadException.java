package com.vladkostromin.restapiapplication.exception;

public class DownloadException extends ApiException {
    public DownloadException(String message, String errorCode) {
        super(message, errorCode);
    }
}
