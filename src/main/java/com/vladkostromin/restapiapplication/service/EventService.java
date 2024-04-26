package com.vladkostromin.restapiapplication.service;

import com.vladkostromin.restapiapplication.entity.EventEntity;
import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.enums.Status;
import com.vladkostromin.restapiapplication.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;


    public Mono<EventEntity> getEventById(Long id) {
        eventRepository.findById(id).flatMap(event -> {
            if(event.getStatus().equals(Status.INACTIVE)) {
                return Mono.error(new RuntimeException());
            }
            return Mono.just(event);
        });
        return eventRepository.findById(id);
    }

    public Mono<EventEntity> createEvent(UserEntity user, FileEntity file) {
        return eventRepository.save(EventEntity.builder()
                        .user(user)
                        .file(file)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(null)
                        .status(Status.ACTIVE)
                .build());
    }

    public Mono<EventEntity> updateEvent(EventEntity event) {
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    public Mono<Void> deleteEvent(EventEntity event) {
        return eventRepository.delete(event);
    }

    public Mono<EventEntity> safeDeleteEvent(Long id) {
        return eventRepository.findById(id)
                .flatMap(event -> {
                    event.setStatus(Status.INACTIVE);
                    return eventRepository.save(event);
                });
    }

}
