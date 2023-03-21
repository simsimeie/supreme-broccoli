package com.search.core.exception;


import com.search.core.constant.Code;
import lombok.Getter;

@Getter
public class KBException extends RuntimeException{

    private final Code code;

    public KBException(Code code) {
        super();
        this.code = code;
    }

    public KBException(Code code, String message ) {
        super(message);
        this.code = code;
    }

    public KBException(Code code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public KBException(Code code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public KBException(Code code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

}
