package hotel_system.model.mapper;

import hotel_system.dto.AmenityRequest;
import hotel_system.dto.AmenityResponse;
import hotel_system.model.entity.Amenity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AmenityMapper {

    @Mapping(source = "id", target = "amenityId")
    AmenityResponse toResponse(Amenity amenity);

    List<AmenityResponse> toResponseList(List<Amenity> amenities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    Amenity toEntity(AmenityRequest request);
}