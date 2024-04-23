package com.vladkostromin.restapiapplication.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtHandler jwtHandler;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Function<String, Mono<String>> bearerTokeValue = value -> Mono.justOrEmpty(value.substring(BEARER_PREFIX.length()));
    private static final Function<ServerWebExchange, Mono<String>> header = exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
                .flatMap(header)
                .flatMap(bearerTokeValue)
                .flatMap(jwtHandler::validateToken)
                .flatMap(UserAuthenticationBearer::fromVerificationResultToAuthentication);
    }
}
