package com.vladkostromin.restapiapplication.mapper;

import com.vladkostromin.restapiapplication.dto.UserDto;
import com.vladkostromin.restapiapplication.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(UserEntity entity);

    @InheritInverseConfiguration
    UserEntity map(UserDto dto);
}
