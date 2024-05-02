package com.vladkostromin.restapiapplication;

import com.vladkostromin.restapiapplication.entity.EventEntity;
import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.enums.Role;
import com.vladkostromin.restapiapplication.enums.UserStatus;
import com.vladkostromin.restapiapplication.repository.EventRepository;
import com.vladkostromin.restapiapplication.repository.UserRepository;
import com.vladkostromin.restapiapplication.service.EventService;
import com.vladkostromin.restapiapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventService eventService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    public void init() {
        this.user = UserEntity.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .firstName("firstNameTest")
                .lastName("lastNameTest")
                .username("usernameTest")
                .password("passwordTest")
                .email("emailTest@gmail.com")
                .role(Role.USER)
                .events(new ArrayList<>())
                .build();


    }

    @Test
    public void getUserByUserIdTest() {
        when(userRepository.findById(anyLong())).thenReturn(Mono.just(user));
        when(eventService.getAllEventsByUserId(anyLong())).thenReturn(Flux.just(new EventEntity()));

        Mono<UserEntity> result = userService.getUserById(1L);

        StepVerifier.create(result)
                        .expectNextMatches(userEntity -> userEntity.equals(user))
                        .verifyComplete();
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getUserByUsernameTest() {
        String username = "usernameTest";
        when(userRepository.findByUsername(username)).thenReturn(Mono.just(user));
        Mono<UserEntity> result = userService.getUserByUsername(username);
        StepVerifier.create(result)
                .expectNextMatches(userEntity -> userEntity.getUsername().equals(username))
                .verifyComplete();
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void createUserTest() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(user));
        Mono<UserEntity> result = userService.createUser(new UserEntity());
        StepVerifier.create(result)
                .expectNextMatches(userEntity -> userEntity.equals(user))
                .verifyComplete();
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void safeDeleteUserTest() {
        user.setStatus(UserStatus.DELETED);
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(user));
        when(userRepository.findById(anyLong())).thenReturn(Mono.just(user));
        Mono<UserEntity> result = userService.safeDeleteUser(1L);
        StepVerifier.create(result)
                .expectNextMatches(userEntity -> userEntity.getStatus().equals(UserStatus.DELETED))
                .verifyComplete();
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void updateUserTest() {
        UserEntity updatedUser = UserEntity.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusSeconds(2))
                .status(UserStatus.ACTIVE)
                .firstName("firstNameTest")
                .lastName("updatedUser")
                .username("usernameTest")
                .password("passwordTest")
                .email("emailTest@gmail.com")
                .role(Role.USER)
                .events(new ArrayList<>())
                .build();
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(updatedUser));
        Mono<UserEntity> result = userService.updateUser(user);
        StepVerifier.create(result)
                        .assertNext(user -> {
                            assertEquals("updatedUser", user.getLastName());
                            assertNotNull(user.getUpdatedAt());
                            assertTrue(user.getUpdatedAt().isAfter(this.user.getUpdatedAt()));
                        }).verifyComplete();
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void deleteUserTest() {
        when(userRepository.deleteById(anyLong())).thenReturn(Mono.empty());
        Mono<Void> result = userService.deleteUser(1L);
        StepVerifier.create(result).verifyComplete();
        verify(userRepository, times(1)).deleteById(anyLong());

    }
}
