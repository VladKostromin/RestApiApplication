package com.vladkostromin.restapiapplication;

import com.vladkostromin.restapiapplication.entity.EventEntity;
import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.enums.EventStatus;
import com.vladkostromin.restapiapplication.repository.EventRepository;
import com.vladkostromin.restapiapplication.repository.FileRepository;
import com.vladkostromin.restapiapplication.repository.UserRepository;
import com.vladkostromin.restapiapplication.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private EventService eventService;

    private EventEntity event;

    @BeforeEach
    public void init() {
        this.event = EventEntity.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(new UserEntity())
                .file(new FileEntity())
                .userId(1L)
                .fileId(1L)
                .build();
    }

    @Test
    public void getEventByIdTest() {
        when(eventRepository.findById(anyLong())).thenReturn(Mono.just(event));
        when(fileRepository.findById(anyLong())).thenReturn(Mono.just(new FileEntity()));
        when(userRepository.findById(anyLong())).thenReturn(Mono.just(new UserEntity()));
        Mono<EventEntity> result = eventService.getEventById(1L);
        StepVerifier.create(result)
                .assertNext(eventEntity -> {
                    assertNotNull(eventEntity.getUser());
                    assertNotNull(eventEntity.getFile());
                    assertEquals(eventEntity, event);
                }).verifyComplete();
        verify(eventRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getAllEventsByUserIdTest() {
        List<EventEntity> eventEntityList = List.of(event, event, event, event);
        when(eventRepository.findAllByUserId(anyLong())).thenReturn(Flux.fromIterable(eventEntityList));
        Flux<EventEntity> result = eventService.getAllEventsByUserId(1L);
        StepVerifier.create(result)
                .expectNext(eventEntityList.get(0), eventEntityList.get(1), eventEntityList.get(2), eventEntityList.get(3))
                .verifyComplete();
        verify(eventRepository, times(1)).findAllByUserId(anyLong());
    }

    @Test
    public void createEventTest() {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(event));
        Mono<EventEntity> result = eventService.createEvent(event);
        StepVerifier.create(result)
                .expectNextMatches(eventEntity -> eventEntity.equals(event))
                .verifyComplete();
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }
    @Test
    public void updateEventTest() {
        EventEntity updatedEvent = EventEntity.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusSeconds(1))
                .user(new UserEntity())
                .file(new FileEntity())
                .userId(1L)
                .fileId(1L)
                .build();
        when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(updatedEvent));
        Mono<EventEntity> result = eventService.updateEvent(event);
        StepVerifier.create(result)
                .assertNext(eventEntity -> assertTrue(eventEntity.getUpdatedAt().isAfter(event.getUpdatedAt())))
                .verifyComplete();
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    public void safeDeleteEventTest() {
        event.setStatus(EventStatus.DELETED);
        when(eventRepository.findById(anyLong())).thenReturn(Mono.just(event));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(event));
        Mono<EventEntity> result = eventService.safeDeleteEvent(1L);
        StepVerifier.create(result)
                .expectNextMatches(eventEntity -> eventEntity.getStatus().equals(EventStatus.DELETED))
                .verifyComplete();
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    public void deleteEventTest() {
        when(eventRepository.deleteById(anyLong())).thenReturn(Mono.empty());
        Mono<Void> result = eventService.deleteEvent(1L);
        StepVerifier.create(result).verifyComplete();
        verify(eventRepository, times(1)).deleteById(anyLong());
    }
}
