package com.github.tuding.blindbox.infrastructure.client;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WxToken {

    private String access_token;
    private Integer expires_in;

}
