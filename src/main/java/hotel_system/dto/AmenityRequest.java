package hotel_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AmenityRequest(
        @NotBlank
        String name,
        @Positive
        double price
) {
}
