package com.hotelbooking.backend.mappers;

import com.hotelbooking.backend.domain.dto.UserDto;
import com.hotelbooking.backend.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserEntity toEntity(UserDto userDto);
    UserDto toDto(UserEntity userEntity);
}
