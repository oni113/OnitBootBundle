package net.nonworkspace.demo.exception.common;

import lombok.Getter;

@Getter
public class CommonBizException extends RuntimeException {

    private final CommonBizExceptionCode errorCode;
    
    public CommonBizException(CommonBizExceptionCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
    }
    
    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
