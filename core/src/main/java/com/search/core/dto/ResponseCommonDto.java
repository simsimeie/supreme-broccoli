package com.search.core.dto;

import com.search.core.constant.Code;
import com.search.core.constant.CodeCategory;
import lombok.Getter;

@Getter
public class ResponseCommonDto {
    private final Boolean ok;
    private final String code;
    private final String message;
    public ResponseCommonDto(Code code){
        if(code.getCategory() == CodeCategory.SUCCESS){
            ok = Boolean.TRUE;
        } else{
            ok = Boolean.FALSE;
        }
        this.code = code.getCode();
        message = code.getMessage();
    }

    public ResponseCommonDto(Code code, String message){
        if(code.getCategory() == CodeCategory.SUCCESS){
            ok = Boolean.TRUE;
        } else{
            ok = Boolean.FALSE;
        }
        this.code = code.getCode();
        this.message = message;
    }

    public ResponseCommonDto(Boolean ok, String code, String message) {
        this.ok = ok;
        this.code = code;
        this.message = message;
    }
}
