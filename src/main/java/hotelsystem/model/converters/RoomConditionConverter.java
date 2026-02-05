package hotelsystem.model.converters;

import hotelsystem.enums.RoomCondition;
import jakarta.persistence.AttributeConverter;

public class RoomConditionConverter implements AttributeConverter<RoomCondition, String> {
    @Override
    public String convertToDatabaseColumn(RoomCondition attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public RoomCondition convertToEntityAttribute(String dbData) {
        return RoomCondition.fromValue(dbData);
    }
}