package hotel_system.model.mapper;

import hotel_system.dto.AmenityOrderRequest;
import hotel_system.dto.AmenityOrderResponse;
import hotel_system.model.entity.AmenityOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AmenityOrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "amenityId", target = "amenityId")
    @Mapping(source = "serviceDate", target = "serviceDate")
    @Mapping(source = "checkInDate", target = "checkInDate")
    @Mapping(source = "checkOutDate", target = "checkOutDate")
    AmenityOrderResponse toResponse(AmenityOrder order);

    List<AmenityOrderResponse> toResponseList(List<AmenityOrder> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "amenityId", source = "amenityId")
    @Mapping(target = "serviceDate", source = "serviceDate")
    @Mapping(target = "checkInDate", source = "checkInDate")
    @Mapping(target = "checkOutDate", source = "checkOutDate")
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "amenity", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "availableDate", ignore = true)
    AmenityOrder toEntity(AmenityOrderRequest request);
}