package hotel_system.model.mapper;

import hotel_system.dto.RoomRequest;
import hotel_system.dto.RoomResponse;
import hotel_system.model.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    @Mapping(source = "number", target = "number")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "priceForDay", target = "price")
    @Mapping(source = "capacity", target = "capacity")
    @Mapping(source = "roomCondition", target = "roomCondition")
    @Mapping(source = "stars", target = "stars")
    @Mapping(target = "isAvailable", expression = "java(room.isAvailable())")
    @Mapping(target = "availableDate", source = "availableDate")
    RoomResponse toResponse(Room room);

    List<RoomResponse> toResponseList(List<Room> rooms);

    @Mapping(source = "number", target = "number")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "price", target = "priceForDay")
    @Mapping(source = "capacity", target = "capacity")
    @Mapping(target = "stars", ignore = true)
    @Mapping(target = "roomCondition", constant = "READY")
    @Mapping(target = "available", constant = "true")
    @Mapping(target = "availableDate", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "hotelConfig", ignore = true)
    Room toEntity(RoomRequest request);
}