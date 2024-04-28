package com.vladkostromin.restapiapplication.repository;

import com.vladkostromin.restapiapplication.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

public interface EventRepository extends R2dbcRepository<EventEntity, Long> {
    Flux<EventEntity> findAllByUserId(Long userId);
}
