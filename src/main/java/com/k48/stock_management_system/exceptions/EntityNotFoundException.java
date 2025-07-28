package com.k48.stock_management_system.exceptions;

import ch.qos.logback.core.spi.ErrorCodes;

public class EntityNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public EntityNotFoundException(String message) {
       super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message,Throwable cause,ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    public EntityNotFoundException(String message,ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
