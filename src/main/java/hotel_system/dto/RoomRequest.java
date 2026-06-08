package hotel_system.dto;

import hotel_system.enums.RoomType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomRequest(
    @Positive(message = "Room number must be positive")
    int number,
    @NotNull
    RoomType type,
    @Positive(message = "Price must be positive")
    double price,
    @Positive(message = "Capacity must be positive")
    int capacity
) {
}
