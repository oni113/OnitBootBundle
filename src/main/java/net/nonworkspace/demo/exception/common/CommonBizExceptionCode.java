package net.nonworkspace.demo.exception.common;

import lombok.Getter;

@Getter
public enum CommonBizExceptionCode {
    INVALID_INPUT("ERR_COM_001", "잘못된 값 입력"),
    DATA_NOT_FOUND("ERR_COM_002", "데이터가 존재하지 않습니다"),
    DATA_EMAIL_DUPLICATE("ERR_COM_003", "중복된 이메일 값 입력"),
    INVALID_PASSWORD_FORMAT("ERR_COM_004", "잘못된 패스워드 값 형식"),
    INVALID_EMAIL_FORMAT("ERR_COM_005", "잘못된 이메일 값 형식"),
    
    NOT_EXIST_MEMBER("ERR_LOGIN_001", "회원 정보가 존재하지 않습니다."),
    NOT_EXIST_LOGIN_EMAIL("ERR_LOGIN_002", "이메일이 존재하지 않습니다."),
    PASSWORD_NOT_MATCHED("ERR_LOGIN_003", "패스워드가 일치하지 않습니다."),
    PASSWORD_EXPIRED("ERR_LOGIN_004", "패스워드 유효 기간 만료"),
    
    PASSWORD_INPUT_NOT_MATCHED("ERR_JOIN_003", "입력한 패스워드가 서로 일치하지 않습니다."),

    ACCESS_NOT_ALLOWED("ERR_AUTH_001", "접근 권한이 없습니다.");
    
    private final String code;
    
    private final String message;
    
    CommonBizExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
