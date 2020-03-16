package com.github.tuding.blindbox.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    String errorMsg;
}
