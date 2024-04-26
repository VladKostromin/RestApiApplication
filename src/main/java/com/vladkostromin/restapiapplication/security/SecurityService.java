package com.vladkostromin.restapiapplication.security;


import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.enums.Status;
import com.vladkostromin.restapiapplication.exception.InactiveException;
import com.vladkostromin.restapiapplication.exception.IncorrectUsernamePasswordException;
import com.vladkostromin.restapiapplication.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expiration;

    private TokenDetails createToken(UserEntity user) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("role", user.getRole());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiredAt(expirationDate)
                .userId(user.getId())
                .build();

    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user -> {
                    if(user.getStatus().equals(Status.INACTIVE)) {
                        return Mono.error(new InactiveException("User is disabled", "ERROR_CODE_INACTIVE"));
                    }
                    if(!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new IncorrectUsernamePasswordException("Username or password is incorrect", "ERROR_CODE_INCORRECT_PASSWORD"));
                    }
                    return Mono.just(createToken(user));
                })
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Username not found")));
    }
}
