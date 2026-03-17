package hotel_system.dto.DtoMethod;

import java.util.Date;

public record AddAmenityRequest(
        String clientId,
        String amenityId,
        Date serviceDate
) {}
