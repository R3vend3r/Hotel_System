package hotel_system.dto;

import java.util.Date;

public record AmenityOrderRequest(
        String clientId,
        double totalPrice,
        String amenityId,
        Date serviceDate,
        Date checkInDate,
        Date checkOutDate
) {
}
