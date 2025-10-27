package com.server.smzshop.common.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        HttpStatus status,
        String code,
        String message,
        LocalDateTime timestamp
) {
    public ErrorResponse(BusinessException errorCode) {
        this(
                errorCode.getError().getStatus(),
                errorCode.getError().getCode(),
                errorCode.getMessage(),
                LocalDateTime.now()
        );
    }

    public ErrorResponse(ExceptionEnum errorEnum){
        this(errorEnum.getStatus(),
            errorEnum.getCode(),
            errorEnum.getMessage(),
            LocalDateTime.now()
        );
    }

    public ErrorResponse(HttpStatus status, String code , String message){
        this(status, code, message, LocalDateTime.now());
    }

}
