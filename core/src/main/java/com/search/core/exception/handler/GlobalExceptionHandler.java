package com.search.core.exception.handler;



import com.search.core.constant.ErrorCode;
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
        ResponseCommonDto errorResponse = getResponseCommonDto(ex);

        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }

    private HttpStatus getHttpStatus(KBException ex){
        ErrorCode errorCode = ex.getErrorCode();
        if(errorCode.isClientSideError()){
            return HttpStatus.BAD_REQUEST;
        }else{
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
    private ResponseCommonDto getResponseCommonDto(KBException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        if(ex.getMessage() != null){
            return new ResponseCommonDto(errorCode, ex.getMessage());
        }
        return new ResponseCommonDto(errorCode, errorCode.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(ConstraintViolationException ex){
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        ResponseCommonDto errorResponse = new ResponseCommonDto(errorCode, ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(DataAccessException.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(DataAccessException ex){
        ErrorCode errorCode = ErrorCode.DB_ERROR;
        ResponseCommonDto errorResponse = new ResponseCommonDto(errorCode);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        ResponseCommonDto errorResponse = new ResponseCommonDto(ErrorCode.INTERNAL_SERVER_ERROR);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ResponseCommonDto>> handleGlobalException(MethodArgumentNotValidException ex){
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        ResponseCommonDto errorResponse = new ResponseCommonDto(errorCode, ex.getFieldError().getDefaultMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }
}