package com.server.smzshop.common.exception;

import jakarta.transaction.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomException (BusinessException e){
        ErrorResponse errorResponse = new ErrorResponse(e);

        return ResponseEntity.status(errorResponse.status())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
            log.error("Validation error - {}: {}", error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(Map.of(
                "status", "BAD_REQUEST",
                "message", "입력값 검증 실패",
                "errors", errors
        ));
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
