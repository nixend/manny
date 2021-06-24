package com.nixend.manny.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @author panyox
 */
public class JwtUtils {

    private static final String SECRET_KEY = "KQylr2p9TuZq0Aww";

    private static final int DEFAULT_EXPIRE = 3;

    private static final String ISSUER = "Manny";

    public static String createToken(String id) {
        try {
            Date date = DateUtils.afterHours(DEFAULT_EXPIRE);
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(id)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            //ignore
            return null;
        }
    }

    public static String parseToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            return null;
        }
    }
}
