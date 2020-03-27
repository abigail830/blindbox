package com.github.tuding.blindbox.infrastructure.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.tuding.blindbox.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtTest {

    @Test
    void verifyWxToken() {
        //givne
        final Jwt jwt = new Jwt();
        final String token = jwt.generateWxToken(new User("openId", "sessionKey"));
        //when
        final DecodedJWT decodedJWT = jwt.verifyWxToken(token);
        //then
        assertEquals("openId", decodedJWT.getSubject());
        assertEquals("sessionKey", decodedJWT.getClaim(Jwt.S_KEY).asString());
    }

    @Test
    void verifyAdminToken() {
        //givne
        final Jwt jwt = new Jwt();
        final String token = jwt.generateAdminToken("admin", "", 10);
        //when
        final String subject = jwt.verifyWxToken(token).getSubject();
        //then
        assertEquals("admin", subject);
    }

    @Test
    void generateToken() {
        final Jwt jwt = new Jwt();
        final String token = jwt.generateWxToken(new User("o4G6g4voOoZEkNeRuDDhVwf0p1IQ", "sessionKey"));

        System.out.println(token);
    }
}