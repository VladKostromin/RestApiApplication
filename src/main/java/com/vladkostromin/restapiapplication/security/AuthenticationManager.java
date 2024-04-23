package com.vladkostromin.restapiapplication.security;


import com.vladkostromin.restapiapplication.enums.Status;
import com.vladkostromin.restapiapplication.exception.UnauthorizedException;
import com.vladkostromin.restapiapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserByUsername(username)
                .filter(user -> user.getStatus().equals(Status.ACTIVE))
                .switchIfEmpty(Mono.error(new UnauthorizedException("User is inactive")))
                .map(user -> authentication);
    }
}
