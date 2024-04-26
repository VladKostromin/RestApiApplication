package com.vladkostromin.restapiapplication.service;

import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.enums.Role;
import com.vladkostromin.restapiapplication.enums.Status;
import com.vladkostromin.restapiapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
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
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(null)
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
                    userEntity.setStatus(Status.INACTIVE);
                    return userRepository.save(userEntity);
                });
    }

    public Mono<UserEntity> updateUser(UserEntity user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }


}
