package com.vladkostromin.restapiapplication.service;

import com.vladkostromin.restapiapplication.dto.UserDto;
import com.vladkostromin.restapiapplication.entity.EventEntity;
import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.enums.Role;
import com.vladkostromin.restapiapplication.enums.UserStatus;
import com.vladkostromin.restapiapplication.mapper.UserMapper;
import com.vladkostromin.restapiapplication.repository.EventRepository;
import com.vladkostromin.restapiapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public Mono<UserDto> getUserById(Long id) {
        return Mono.zip(userRepository.findById(id),
                eventRepository.findAllByUserId(id).collectList())
                .map(tuples -> {
                    UserEntity user = tuples.getT1();
                    List<EventEntity> events = tuples.getT2();
                    user.setEvents(events);
                    return userMapper.map(user);
                });
    }

    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Mono<UserEntity> createUser(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .username(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(Role.USER)
                        .status(UserStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .events(new ArrayList<>())
                        .build()
        );
    }
    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<UserEntity> safeDeleteUser(Long id) {
        return userRepository
                .findById(id)
                .flatMap(userEntity -> {
                    userEntity.setStatus(UserStatus.DELETED);
                    return userRepository.save(userEntity);
                });
    }

    public Mono<UserEntity> updateUser(UserEntity user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }


}
