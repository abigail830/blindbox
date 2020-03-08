package com.github.tuding.blindbox.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JWTTokenHandlerTest {

    @Test
    void verifyWxToken() {
        //givne
        final JWTTokenHandler jwtTokenHandler = new JWTTokenHandler();
        final String token = jwtTokenHandler.generateWxToken("openId");
        //when
        final String subject = jwtTokenHandler.verifyWxToken(token).getSubject();
        //then
        assertEquals("openId", subject);
    }

    @Test
    void verifyAdminToken() {
        //givne
        final JWTTokenHandler jwtTokenHandler = new JWTTokenHandler();
        final String token = jwtTokenHandler.generateAdminToken("admin", "", 10);
        //when
        final String subject = jwtTokenHandler.verifyWxToken(token).getSubject();
        //then
        assertEquals("admin", subject);
    }
}