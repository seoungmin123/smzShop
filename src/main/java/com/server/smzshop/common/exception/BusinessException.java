package com.server.smzshop.common.exception;

import lombok.Getter;

/**
 * 비즈니스 로직, 내부 오류 예외처리 -> 기본 Rollback
 * UncheckedException
 * */
@Getter
public class BusinessException extends RuntimeException{

    private ExceptionEnum error;
    private String[] args;

    public BusinessException(ExceptionEnum error, String ...args) {
        super(buildMessage(error.getMessage(), args));
        this.error = error;
        this.args = args;
    }

    private static String buildMessage(String template, String... args){
        if (args.length == 0){
            return template;
        }

        String result = template;
        for (String arg : args){
            result = result.replaceFirst("%s",arg);
        }
        return result;
    }


}
