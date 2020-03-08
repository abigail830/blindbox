package com.github.tuding.blindbox.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.tuding.blindbox.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtTokenHandler {

    /**
     * 1 hour
     */
    private static final long EXPIRATION_TIME = 3600000;
    /**
     * JWT密码
     */
    private static final String DEFAULT_SECRET = "secret";
    private static final String ENV_SECRET_KEY = "ENCRYPTION_KEY_WL";
    private static final String ISSUER = "blindbox";

    String getSecret() {
        return Optional.ofNullable(System.getenv(ENV_SECRET_KEY))
                .orElse(DEFAULT_SECRET);
    }

    public String generateAdminToken(String username, String key, long age) {
        Date today = new Date();
        Date expiresAt = new Date(today.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withIssuer("blindbox")
                .withSubject("admin")
                .withClaim("user", username)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(getSecret()));

    }

    public String generateWxToken(User user) {
        final Date today = new Date();
        final Date expiresAt = new Date(today.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withSubject(user.getOpenId())
                .withIssuer(ISSUER)
                .withIssuedAt(today)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(getSecret()));
    }

    public DecodedJWT verifyWxToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(getSecret());
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        return verifier.verify(token);
    }
}
