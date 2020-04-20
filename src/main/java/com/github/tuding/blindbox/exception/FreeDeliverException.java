package com.github.tuding.blindbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "free deliver")
public class FreeDeliverException extends RuntimeException {
}
