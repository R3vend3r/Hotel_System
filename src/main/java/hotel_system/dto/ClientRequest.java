package hotel_system.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Surname cannot be blank")
        String surname
) {
}
