package com.github.tuding.blindbox.exception;


import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
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

        if (e.getErrorCode().equals(ErrorCode.WX_USER_NOT_FOUND)) {
            return new ResponseEntity<>(JsonUtil.toJson(new Object()), HttpStatus.OK);
        } else {
            final ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().name());
            return new ResponseEntity<>(JsonUtil.toJson(errorResponse), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<?> genericExceptionHandler(Exception e) {
        log.warn("{}", e);
        final ErrorResponse errorResponse = new ErrorResponse("UNKNOWN");
        return new ResponseEntity<>(JsonUtil.toJson(errorResponse), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
