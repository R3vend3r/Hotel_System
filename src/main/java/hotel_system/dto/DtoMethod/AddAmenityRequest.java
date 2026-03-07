package hotel_system.dto.DtoMethod;

import java.util.Date;

public record AddAmenityRequest(
        int roomNumber,
        String amenityId,
        Date serviceDate
) {}
