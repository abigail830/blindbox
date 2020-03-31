package com.github.tuding.blindbox.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.tuding.blindbox.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class Jwt {

    /**
     * 1 hour
     */
    private static final long EXPIRATION_TIME = 3600000;
    /**
     * JWT密码
     */
    private static final String DEFAULT_SECRET = "secret";
    private static final String ENV_SECRET_KEY = "ENCRYPTION_KEY";
    private static final String ISSUER = "blindbox";
    public static final String S_KEY = "sKey";

    Algorithm getAlgorithm() {
        String secret = Optional.ofNullable(System.getenv(ENV_SECRET_KEY))
                .orElse(DEFAULT_SECRET);
        return Algorithm.HMAC512(secret);
    }

    public String generateAdminToken(String username, String key, long age) {
        Date today = new Date();
        Date expiresAt = new Date(today.getTime() + EXPIRATION_TIME);

        String token = JWT.create()
                .withIssuer("blindbox")
                .withSubject("admin")
                .withClaim("user", username)
                .withExpiresAt(expiresAt)
                .sign(getAlgorithm());

        return token;

    }

    public String generateWxToken(User user) {
        final Date today = new Date();
        final Date expiresAt = new Date(today.getTime() + EXPIRATION_TIME);

        String token = JWT.create()
                .withSubject(user.getOpenId())
                .withIssuer(ISSUER)
                .withClaim(S_KEY, user.getSessionKey())
                .withIssuedAt(today)
                .withExpiresAt(expiresAt)
                .sign(getAlgorithm());
        log.info("token for openId[{}] is {}", user.getOpenId(), token);
        return token;
    }

    public DecodedJWT verifyWxToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(ISSUER).build();
        return verifier.verify(token);
    }

    public String getOpenIdFromToken(String token) {
        final DecodedJWT decodedJWT = verifyWxToken(token);
        return decodedJWT.getSubject();
    }

    public String getSKeyFromToken(String token) {
        final DecodedJWT decodedJWT = verifyWxToken(token);
        return decodedJWT.getClaim(S_KEY).asString();
    }
}
