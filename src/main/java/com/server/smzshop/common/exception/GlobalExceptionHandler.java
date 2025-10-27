package com.server.smzshop.common.exception;

import jakarta.transaction.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomException (BusinessException e){
        ErrorResponse errorResponse = new ErrorResponse(e);

        return ResponseEntity.status(errorResponse.status())
                .body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR
                                                , ExceptionEnum.RUNTIME_EXCEPTION.getCode()
                                                , ExceptionEnum.RUNTIME_EXCEPTION.getMessage()
                                                ,LocalDateTime.now());
        return ResponseEntity.status(errorResponse.status())
                .body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e ) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR
            , ExceptionEnum.INERNAL_SERVER_ERROR.getCode()
            , ExceptionEnum.INERNAL_SERVER_ERROR.getMessage()
            , LocalDateTime.now());

        return ResponseEntity.status(errorResponse.status())
                .body(errorResponse);
    }

}
