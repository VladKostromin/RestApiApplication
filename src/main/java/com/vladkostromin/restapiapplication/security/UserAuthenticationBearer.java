package com.vladkostromin.restapiapplication.security;

import com.vladkostromin.restapiapplication.enums.Role;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Set;

public class UserAuthenticationBearer {
    public static Mono<Authentication> fromVerificationResultToAuthentication(JwtHandler.VerificationResult verificationResult) {
        Claims claims = verificationResult.getClaims();
        String userName = claims.getSubject();

        String role = claims.get("role", String.class);
        Set<SimpleGrantedAuthority> authorities = Role.valueOf(role).getAuthorities();

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(userName, null, authorities));
    }

}
