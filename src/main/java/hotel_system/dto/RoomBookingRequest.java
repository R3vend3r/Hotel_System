package hotel_system.dto;

import java.util.Date;

public record RoomBookingRequest(
        String clientId,
        int roomNumber,
        Date checkInDate,
        Date checkOutDate,
        double total
) {

}
