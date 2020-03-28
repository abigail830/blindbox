package com.github.tuding.blindbox.infrastructure.client;


import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class WxTokenResponse {

    private String access_token;
    private Integer expires_in;
    private Instant expiryInstant;

    public WxTokenResponse(String access_token, Integer expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.expiryInstant = Instant.now().plusSeconds(expires_in);
    }

    public Boolean isTokenValid() {
        return !Strings.isNullOrEmpty(access_token)
                && Instant.now().isBefore(expiryInstant);
    }
}
