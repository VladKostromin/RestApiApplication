package com.vladkostromin.restapiapplication.rest;

import com.vladkostromin.restapiapplication.dto.UserDto;
import com.vladkostromin.restapiapplication.dto.authdto.AuthRequestDto;
import com.vladkostromin.restapiapplication.dto.authdto.AuthResponseDto;
import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.mapper.UserMapper;
import com.vladkostromin.restapiapplication.security.SecurityService;
import com.vladkostromin.restapiapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    public Mono<UserDto> registration(@RequestBody UserDto dto) {
        UserEntity userEntity = userMapper.map(dto);
        return userService.createUser(userEntity)
                .map(userMapper::map);
    }
    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiredAt())
                                .build()
                ));
    }
}
