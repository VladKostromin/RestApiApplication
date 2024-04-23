package com.vladkostromin.restapiapplication.repository;

import com.vladkostromin.restapiapplication.entity.FileEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface FileRepository extends R2dbcRepository<FileEntity, Long> {
}
