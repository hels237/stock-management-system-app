package com.k48.stock_management_system.handler;


import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.exceptions.ObjectValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalHandllerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handler(EntityNotFoundException exception,WebRequest webRequest){

        final HttpStatus status= HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse =
                ErrorResponse
                        .builder()
                        .message(exception.getMessage())
                        .httpCode(status.value())
                        .httpCode(status.value())
                        .source(exception.getClass().getName())
                        .build();

        return  new ResponseEntity<>(errorResponse, status);
    }


    @ExceptionHandler(ObjectValidationException.class)
    public ResponseEntity<?> handler(ObjectValidationException exception,WebRequest webRequest){
        final HttpStatus status= HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse =
                ErrorResponse
                        .builder()
                        .message(exception.getMessage())
                        .httpCode(status.value())
                        .source(exception.getViolationSource().getClass().getName())
                        .errorViolations(exception.getViolations()).build();

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<?> handler(InvalidOperationException exception,WebRequest webRequest){
        ErrorResponse errorResponse =
                    ErrorResponse
                            .builder()
                            .message(exception.getMessage())
                            .source(exception.getClass().getName())
                            .build();
        return ResponseEntity.ok(errorResponse);
    }

}
