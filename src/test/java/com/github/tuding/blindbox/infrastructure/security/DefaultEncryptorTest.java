package com.github.tuding.blindbox.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultEncryptorTest {

    @Test
    void encrypt() {
        final DefaultEncryptor defaultEncryptor = new DefaultEncryptor();
        assertEquals("Ox+IjEt3WlNuPwFiPp6plRasA1Ssms2V", defaultEncryptor.encrypt("blindboxtest"));
    }
}