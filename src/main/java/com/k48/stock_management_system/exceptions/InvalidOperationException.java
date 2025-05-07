package com.k48.stock_management_system.exceptions;

public class InvalidOperationException extends RuntimeException {

    private ErrorCode errorCode;

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOperationException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public InvalidOperationException(String message,ErrorCode errorCode ) {
        super(message);
        this.errorCode = errorCode;
    }
    public InvalidOperationException( String message) {
        super(message);
    }
}
