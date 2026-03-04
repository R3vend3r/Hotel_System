package hotel_system.dto;

import java.util.Date;

public record AmenityOrderResponse(
        String id,
        String clientId,
        double totalPrice,
        String amenityId,
        Date serviceDate,
        Date checkInDate,
        Date checkOutDate
) {
}
