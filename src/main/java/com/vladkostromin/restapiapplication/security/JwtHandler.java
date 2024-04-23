package com.vladkostromin.restapiapplication.security;

import com.vladkostromin.restapiapplication.exception.TokenExpirationDateException;
import com.vladkostromin.restapiapplication.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class JwtHandler {

    private final String secret;


    public Mono<VerificationResult> validateToken(String accessToken) {
        return Mono.just(isTokenExpired(accessToken)).onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult isTokenExpired(String token) {
        Claims claims = getClaimsFromJwtToken(token);
        final Date expiration = claims.getExpiration();

        if(expiration.before(new Date())) {
            throw new TokenExpirationDateException("Expired JWT token", "ERROR_CODE_TOKEN_EXPIRED");
        }
        return new VerificationResult(claims, token);
    }

    private Claims getClaimsFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }


    @AllArgsConstructor
    @Getter
    public static class VerificationResult {
        private Claims claims;
        private String token;

    }
}
