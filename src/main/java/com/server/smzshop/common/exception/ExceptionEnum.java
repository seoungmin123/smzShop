package com.server.smzshop.common.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    //system Exception
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "시스템 오류가 발생했습니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002 : %s"),
    INERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"E0003", "알 수 없는 오류가 발생했습니다"),

    // custom Exception
    // 사용자 관련 예외 (U로 시작)
    USER_DUPLICATE(HttpStatus.CONFLICT, "U0001", "이미 등록된 아이디입니다.: %s"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U0002", "사용자를 찾을 수 없습니다. : %s"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U0003", "비밀번호가 일치하지 않습니다."),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "U0004", "비활성화된 계정입니다. : %s"),
    INVALID_USERID_FORMAT(HttpStatus.BAD_REQUEST, "U0005", "아이디 형식이 올바르지 않습니다. : %s"),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "U0006", "비밀번호 형식이 올바르지 않습니다. : %s"),

    // 인증/인가 예외 (A로 시작)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A0001", "인증이 필요합니다. : %s"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A0002", "접근 권한이 없습니다. : %s"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A0003", "토큰이 만료되었습니다. : %s"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A0004", "유효하지 않은 토큰입니다. : %s"),

    // 일반적인 비즈니스 예외 (B로 시작)
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "B0001", "입력값이 올바르지 않습니다. : %s"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "B0002", "요청한 리소스를 찾을 수 없습니다. : %s"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "B0003", "이미 존재하는 리소스입니다. : %s"),

    // 시스템 예외 (S로 시작)
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S0001", "데이터베이스 오류가 발생했습니다. : %s"),
    EXTERNAL_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "S0002", "외부 서비스 호출 실패 : %s");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code){
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message){
        this(status, code);
        this.message = message;
    }

}
