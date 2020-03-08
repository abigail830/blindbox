package com.github.tuding.blindbox.infrastructure.security;

import com.github.tuding.blindbox.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtTokenHandlerTest {

    @Test
    void verifyToken() {
        //givne
        final User user = new User("openId");
        final JwtTokenHandler jwtTokenHandler = new JwtTokenHandler();
        final String token = jwtTokenHandler.generateWxToken(user);
        //when
        final String subject = jwtTokenHandler.verifyWxToken(token).getSubject();
        //then
        assertEquals("openId", subject);
    }
}