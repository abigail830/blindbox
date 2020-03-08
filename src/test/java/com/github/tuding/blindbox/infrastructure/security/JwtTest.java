package com.github.tuding.blindbox.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtTest {

    @Test
    void verifyWxToken() {
        //givne
        final Jwt jwt = new Jwt();
        final String token = jwt.generateWxToken("openId");
        //when
        final String subject = jwt.verifyWxToken(token).getSubject();
        //then
        assertEquals("openId", subject);
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
}