package com.github.tuding.blindbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "draw temporarily not available, try again ")
public class DrawException extends RuntimeException {
}
