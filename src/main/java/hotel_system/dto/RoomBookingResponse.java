package hotel_system.dto;

import java.util.Date;

public record RoomBookingResponse(
        String id,
        String clientId,
        int roomNumber,
        Date checkInDate,
        Date checkOutDate,
        double total
) {
}
