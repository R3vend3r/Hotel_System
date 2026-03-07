package hotel_system.dto;

import hotel_system.enums.RoomCondition;
import hotel_system.enums.RoomType;

import java.util.Date;

public record RoomResponse(
        int number,
        RoomType type,
        double price,
        int capacity,
        RoomCondition roomCondition,
        int stars,
        boolean isAvailable,
        Date availableDate
) {
}
