package com.hotelbooking.backend.mappers;

import com.hotelbooking.backend.domain.dto.authentication.BookingRequest;
import com.hotelbooking.backend.domain.dto.authentication.BookingResponse;
import com.hotelbooking.backend.domain.entities.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "room.id", target = "roomId")
    BookingResponse toDto(BookingEntity bookingEntity);

    List<BookingResponse> toDtoList(List<BookingEntity> entities);
}
