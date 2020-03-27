package com.github.tuding.blindbox.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BizException extends RuntimeException {

    ErrorCode errorCode;

    public BizException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
