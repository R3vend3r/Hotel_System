package hotel_system.model.converters;

import hotel_system.enums.RoomType;
import jakarta.persistence.AttributeConverter;

public class RoomTypeConverter implements AttributeConverter<RoomType, String> {
    @Override
    public String convertToDatabaseColumn(RoomType roomType) {
        return roomType != null ? roomType.getValue() : null;
    }

    @Override
    public RoomType convertToEntityAttribute(String s) {
        return RoomType.fromValue(s);
    }
}