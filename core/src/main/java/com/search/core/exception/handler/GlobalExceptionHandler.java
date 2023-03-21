package com.search.core.exception.handler;



import com.search.core.constant.Code;
import com.search.core.dto.ResponseCommonDto;
import com.search.core.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(KBException.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(KBException ex) {
        HttpStatus status = getHttpStatus(ex);
        ResponseCommonDto response = getResponseCommonDto(ex);

        return Mono.just(ResponseEntity.status(status).body(response));
    }

    private HttpStatus getHttpStatus(KBException ex){
        Code code = ex.getCode();
        if(code.isSuccess()){
            return HttpStatus.OK;
        } else if(code.isClientSideError()){
            return HttpStatus.BAD_REQUEST;
        } else{
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
    private ResponseCommonDto getResponseCommonDto(KBException ex) {
        Code code = ex.getCode();
        if(ex.getMessage() != null){
            return new ResponseCommonDto(code, ex.getMessage());
        }
        return new ResponseCommonDto(code, code.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(ConstraintViolationException ex){
        Code code = Code.BAD_REQUEST;
        ResponseCommonDto errorResponse = new ResponseCommonDto(code, ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(DataAccessException.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(DataAccessException ex){
        Code code = Code.DB_ERROR;
        ResponseCommonDto errorResponse = new ResponseCommonDto(code);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        ResponseCommonDto errorResponse = new ResponseCommonDto(Code.INTERNAL_SERVER_ERROR);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    // 추후 추가될 수 있는 Post 방식으로 들어오는 값에 대해 Validation exception handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(MethodArgumentNotValidException ex){
        Code code = Code.BAD_REQUEST;
        ResponseCommonDto errorResponse = new ResponseCommonDto(code, ex.getFieldError().getDefaultMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }
}