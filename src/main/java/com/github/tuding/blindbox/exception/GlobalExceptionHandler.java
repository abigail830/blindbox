package com.github.tuding.blindbox.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResponseEntity<?> bizExceptionHandler(BizException e) {
        log.warn("{}", e);
        return new ResponseEntity<>(e.getErrorCode().name(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<?> genericExceptionHandler(Exception e) {
        log.warn("{}", e);
        return new ResponseEntity<>("UNKNOWN", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}