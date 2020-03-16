package com.github.tuding.blindbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "series not found")
public class SeriesNotFoundException extends RuntimeException  {
}
