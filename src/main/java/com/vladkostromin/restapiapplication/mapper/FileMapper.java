package com.vladkostromin.restapiapplication.mapper;

import com.vladkostromin.restapiapplication.dto.FileDto;
import com.vladkostromin.restapiapplication.entity.FileEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDto map(FileEntity file);
    @InheritInverseConfiguration
    FileEntity map(FileDto dto);

}
