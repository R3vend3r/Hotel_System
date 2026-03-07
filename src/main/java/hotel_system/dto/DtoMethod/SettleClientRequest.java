package hotel_system.dto.DtoMethod;

import java.util.Date;

public record SettleClientRequest(
        String clientId,
        int roomNumber,
        Date checkOutDate
) {}