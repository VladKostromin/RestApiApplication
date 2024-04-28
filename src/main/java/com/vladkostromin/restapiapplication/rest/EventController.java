package com.vladkostromin.restapiapplication.rest;

import com.vladkostromin.restapiapplication.dto.EventDto;
import com.vladkostromin.restapiapplication.entity.EventEntity;
import com.vladkostromin.restapiapplication.mapper.EventMapper;
import com.vladkostromin.restapiapplication.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'moderator:read')")
    public Mono<EventDto> getById(@PathVariable Long id) {
        return eventService.getEventById(id).map(eventMapper::map);
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Mono<EventDto> updateEvent(@RequestBody EventDto eventDto) {
        EventEntity eventEntity = eventMapper.map(eventDto);
        return eventService.updateEvent(eventEntity).map(eventMapper::map);
    }
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:update')")
    public Mono<EventDto> createEvent(@RequestBody EventDto eventDto) {
        EventEntity eventEntity = eventMapper.map(eventDto);
        return eventService.createEvent(eventEntity).map(eventMapper::map);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Mono<Void> delete(@PathVariable Long id) {
        return eventService.deleteEvent(id);
    }
}
