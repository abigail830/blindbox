package com.github.tuding.blindbox.infrastructure.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class Jwt {
    public static String createToken(String username, String key, long age) {
        String secret = System.getenv("ENCRYPTION_KEY_WL");
        Date today = new Date();
        Date expiresAt = new Date(today.getTime() + 3600000);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer("blindbox")
                .withSubject("admin")
                .withClaim("user", username)
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return token;

    }

    public static DecodedJWT verify(String token) {
        String secret = System.getenv("ENCRYPTION_KEY_WL");

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).withIssuer("blindbox").build();
        DecodedJWT verify = jwtVerifier.verify(token);
        return verify;
    }
}
