package com.search.core.exception;


import com.search.core.constant.ErrorCode;
import lombok.Getter;

@Getter
public class KBException extends RuntimeException{

    private final ErrorCode errorCode;

    public KBException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public KBException(ErrorCode errorCode, String message ) {
        super(message);
        this.errorCode = errorCode;
    }

    public KBException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public KBException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public KBException(ErrorCode errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
