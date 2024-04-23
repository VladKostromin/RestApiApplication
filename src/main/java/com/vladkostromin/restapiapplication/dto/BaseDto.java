package com.vladkostromin.restapiapplication.dto;

import com.vladkostromin.restapiapplication.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDto {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
    private Status status;
}
