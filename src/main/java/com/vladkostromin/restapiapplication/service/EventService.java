package com.vladkostromin.restapiapplication.service;

import com.vladkostromin.restapiapplication.entity.EventEntity;
import com.vladkostromin.restapiapplication.enums.EventStatus;
import com.vladkostromin.restapiapplication.repository.EventRepository;
import com.vladkostromin.restapiapplication.repository.FileRepository;
import com.vladkostromin.restapiapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    public Mono<EventEntity> getEventById(Long id) {
        return eventRepository.findById(id)
                .flatMap(eventEntity -> userRepository.findById(eventEntity.getUserId())
                        .map(userEntity -> {
                            eventEntity.setUser(userEntity);
                            return eventEntity;
                        })
                        .flatMap(event -> fileRepository.findById(event.getFileId()))
                .map(fileEntity -> {
                    eventEntity.setFile(fileEntity);
                    return eventEntity;
                }));
    }

    public Mono<EventEntity> createEvent(EventEntity eventEntity) {
        return eventRepository.save(EventEntity.builder()
                        .user(eventEntity.getUser())
                        .file(eventEntity.getFile())
                        .userId(eventEntity.getUserId())
                        .fileId(eventEntity.getFileId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .status(eventEntity.getStatus())
                .build());
    }

    public Mono<EventEntity> updateEvent(EventEntity event) {
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    public Mono<Void> deleteEvent(Long eventId) {
        return eventRepository.deleteById(eventId);
    }

    public Mono<EventEntity> safeDeleteEvent(Long id) {
        return eventRepository.findById(id)
                .flatMap(event -> {
                    event.setStatus(EventStatus.DELETED);
                    return eventRepository.save(event);
                });
    }

}
