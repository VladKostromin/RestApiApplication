package com.vladkostromin.restapiapplication.rest;

import com.vladkostromin.restapiapplication.dto.UserDto;
import com.vladkostromin.restapiapplication.mapper.UserMapper;
import com.vladkostromin.restapiapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('user:download')")
    public Mono<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(userMapper::map);
    }

}
