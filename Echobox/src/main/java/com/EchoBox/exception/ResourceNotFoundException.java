package com.EchoBox.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends EchoboxException {

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ResourceNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}

