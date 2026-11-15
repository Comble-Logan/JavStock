package com.logistics.exception;

public class NotAPickingException extends RuntimeException {
    public NotAPickingException(String message) {
        super(message);
    }
}
