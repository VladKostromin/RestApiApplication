package com.vladkostromin.restapiapplication.repository;

import com.vladkostromin.restapiapplication.entity.FileEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<FileEntity, Long> {
    Mono<FileEntity> findByFileName(String fileName);
}
