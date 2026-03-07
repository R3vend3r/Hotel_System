package hotel_system.dto;

import hotel_system.enums.RoomType;

public record RoomRequest(
    int number,
    RoomType type,
    double price,
    int capacity
) {
}
