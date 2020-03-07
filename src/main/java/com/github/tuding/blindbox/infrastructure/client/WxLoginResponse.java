package com.github.tuding.blindbox.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class WxLoginResponse {
    private String session_key;
    private String openid;

}
