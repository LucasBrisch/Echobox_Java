package com.EchoBox.exception;

import lombok.Getter;

/**
 * Base exception class for all business logic exceptions in the application.
 * Contains an error code for internationalization support.
 */
@Getter
public class EchoboxException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] args;

    public EchoboxException(ErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.args = null;
    }

    public EchoboxException(ErrorCode errorCode, Object... args) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.args = args;
    }

    public EchoboxException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getCode(), cause);
        this.errorCode = errorCode;
        this.args = null;
    }

    public EchoboxException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode.getCode(), cause);
        this.errorCode = errorCode;
        this.args = args;
    }
}

