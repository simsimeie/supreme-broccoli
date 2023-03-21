package com.search.core.dto;

import com.search.core.constant.Code;
import lombok.Getter;

@Getter
public class ResponseDto<T> extends ResponseCommonDto {
    private T body;
    private ResponseDto(T body){
        super(Code.SUCCESS);
        this.body = body;
    }
    public static <T> ResponseDto<T> of(T body){
        return new ResponseDto(body);
    }

}
