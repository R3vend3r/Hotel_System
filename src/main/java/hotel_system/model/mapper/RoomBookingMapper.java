package hotel_system.model.mapper;

import hotel_system.dto.RoomBookingRequest;
import hotel_system.dto.RoomBookingResponse;
import hotel_system.model.entity.RoomBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomBookingMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "room.number", target = "roomNumber")
    @Mapping(source = "checkInDate", target = "checkInDate")
    @Mapping(source = "checkOutDate", target = "checkOutDate")
    @Mapping(source = "totalPrice", target = "total")
    RoomBookingResponse toResponse(RoomBooking booking);

    List<RoomBookingResponse> toResponseList(List<RoomBooking> bookings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "checkInDate", source = "checkInDate")
    @Mapping(target = "checkOutDate", source = "checkOutDate")
    @Mapping(target = "totalPrice", source = "total")
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "availableDate", ignore = true)
    RoomBooking toEntity(RoomBookingRequest request);
}