package com.vladkostromin.restapiapplication.mapper;

import com.vladkostromin.restapiapplication.dto.EventDto;
import com.vladkostromin.restapiapplication.entity.EventEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto map(EventEntity event);
    @InheritInverseConfiguration
    EventEntity map(EventDto dto);
}
