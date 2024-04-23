package com.vladkostromin.restapiapplication.repository;

import com.vladkostromin.restapiapplication.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface EventRepository extends R2dbcRepository<EventEntity, Long> {
}
