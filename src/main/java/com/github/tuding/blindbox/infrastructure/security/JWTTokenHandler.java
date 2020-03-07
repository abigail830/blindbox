package com.github.tuding.blindbox.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.tuding.blindbox.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JWTTokenHandler {

    /**
     * 5天(毫秒)
     */
    private static final long EXPIRATION_TIME = 432000000;
    /**
     * JWT密码
     */
    private static final String SECRET = "secret";


    public String generateToken(User user) {
        final Date current = new Date(System.currentTimeMillis());
        final Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return JWT.create()
                .withSubject(user.getOpenId())
                .withIssuer("blindbox")
                .withClaim("nickName", Optional.ofNullable(user.getNickName()).orElse(""))
                .withIssuedAt(current)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(SECRET));
    }

    public String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            return verifier.verify(token).getSubject();

        } catch (JWTVerificationException exception) {
            log.error("Invalid signature/claims");
            return null;
        }
    }
}
